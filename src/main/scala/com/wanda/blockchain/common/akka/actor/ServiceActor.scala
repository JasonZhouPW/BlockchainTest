package com.wanda.blockchain.common.akka.actor

import java.io.File

import akka.actor.Actor
import akka.cluster.Cluster
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.Publish
import com.wanda.blockchain.common.akka.cluster.ClusterManager
import com.wanda.blockchain.common.chaincode.{ChainCodeInterface, ChainCodeMgr, NewCCHandlerImpl, NewCCLoader}
import com.wanda.blockchain.common.db.{DBProvider, DBStore}
import com.wanda.blockchain.common.util.FileUtils


/**
  * Created by Zhou peiwen on 2017/7/21.
  */

case class CreateChannelMsg(dbName:String)

case class InstallChainCodeMsg(userName:String,chainname:String,chaincodeName:String,chaincodeVersion:String,chaincodeClassFullName:String,jarFileName:String)

case class InvokeChainCodeMsg(userName:String,chainname:String,chaincodeName:String,chaincodeVersion:String,methodName:String,params:java.util.ArrayList[String])

class ServiceActor extends Actor{

  val mediator = DistributedPubSub(context.system).mediator

  val selfAddress = Cluster(context.system).selfAddress

  val ccDirectory = "c:/work/temp/testjar"

  override def receive: Receive = {

    case msg: CreateChannelMsg =>
      println("CreateChannelMsg")
//      mediator ! Publish(ClusterManager.topicName,new ProposalMessage)
      //got message to create a channel , means to create a new database in local
      val dbname = msg.dbName
      if(DBProvider.dbMap.get(dbname).isEmpty){
        //currently we just create the database
        val db = DBProvider.getDB(dbname)
      }else{
        //tell sender the db is exists
      }

      mediator ! Publish(ClusterManager.topicName,new CreateChannelACKMsg(selfAddress,0,"OK"))


    case msg:InstallChainCodeMsg =>
      //this is for self local install chaincode
      println("InstallChainCodeMsg")

      ChainCodeMgr.installChainCode(msg.chaincodeName,msg.chaincodeClassFullName,ccDirectory + "/" + msg.jarFileName)
      ChainCodeMgr.initialChainCode(msg.userName,msg.chainname,msg.chaincodeName,msg.chaincodeVersion)

      val jarfile = new File(ccDirectory + "/" + msg.jarFileName)
      mediator ! Publish(ClusterManager.topicName,new InstallCCEventMsg(selfAddress,msg.userName,msg.chainname,msg.chaincodeName,msg.chaincodeVersion,msg.chaincodeClassFullName,FileUtils.fileToBytes(jarfile)))


    case msg:InvokeChainCodeMsg =>
      println(s"InvokeChainCodeMsg:$msg")
      //todo to store chaincode based on chain / user ???
      val chaincodeOpt = ChainCodeMgr.getChainCode(msg.chaincodeName)
      chaincodeOpt match {
        case Some(chaincode) =>
          val res = chaincode.invoke(msg.methodName,msg.params)
          println(s"res is $res")

        case None =>
          println(s"no chaincode found :${msg.chaincodeName}")
      }

      mediator ! Publish(ClusterManager.topicName,new InvokeCCEventMsg(msg.userName,msg.chainname,msg.chaincodeName,msg.chaincodeVersion,msg.methodName,msg.params))
  }
}
