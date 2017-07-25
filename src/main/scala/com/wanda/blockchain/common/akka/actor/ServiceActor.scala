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

case class InvokeChainCodeMsg()

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

      //todo how to get selfID??
      mediator ! Publish(ClusterManager.topicName,new CreateChannelACKMsg(selfAddress,0,"OK"))




    case msg:InstallChainCodeMsg =>
      //this is for self local install chaincode
      println("InstallChainCodeMsg")

      ChainCodeMgr.installChainCode(msg.chaincodeName,msg.chaincodeClassFullName,ccDirectory + "/" + msg.jarFileName)
      ChainCodeMgr.initialChainCode(msg.userName,msg.chainname,msg.chaincodeName,msg.chaincodeVersion)

/*      val loader = new NewCCLoader
      loader.loadJarFile(ccDirectory + "/" + msg.jarFileName)

      val chainCode = Class.forName(msg.chaincodeClassFullName).newInstance().asInstanceOf[ChainCodeInterface]

      chainCode.init
      val cchandler =new NewCCHandlerImpl

      cchandler.setDbObject(DBStore.getDB(msg.chainname))
      cchandler.init("admin","mycc","1.0")
      chainCode.setHandler(cchandler)
      println("local install chaincode done!")*/
      val jarfile = new File(ccDirectory + "/" + msg.jarFileName)
      //      sender ! new InstallCCACKMsg(msg.chaincodeName,1,"OK")
      mediator ! Publish(ClusterManager.topicName,new InstallCCEventMsg(selfAddress,msg.userName,msg.chainname,msg.chaincodeName,msg.chaincodeVersion,msg.chaincodeClassFullName,FileUtils.fileToBytes(jarfile)))


    case msg:InvokeChainCodeMsg =>
      println("InvokeChainCodeMsg")
  }
}
