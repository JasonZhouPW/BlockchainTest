package com.wanda.blockchain.common.akka.actor

import java.io.File

import akka.actor.Actor
import akka.cluster.Cluster
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.Publish
import com.wanda.blockchain.common.akka.cluster.ClusterManager
import com.wanda.blockchain.common.block.BlockMgr
import com.wanda.blockchain.common.chaincode.{ChainCodeMgr, NewCCHandlerImpl}
import com.wanda.blockchain.common.consensus.pbft.View
import com.wanda.blockchain.common.db.DBProvider
import com.wanda.blockchain.common.util.{DigestUtil, FileUtils}

import scala.collection.mutable.{Map => mMap}


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

  @volatile private var count:Long = 0

  @volatile private var viewNo:Long = 0

  private var stateMap:mMap[Long,Int] = mMap[Long,Int]()

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

      //make new pbft preprepare message
      val jarfile = new File(ccDirectory + "/" + msg.jarFileName)

      this.viewNo += 1
      val timestamp = System.currentTimeMillis()
      val datahash = DigestUtil.digest(FileUtils.fileToBytes(jarfile))
      val view = new View(this.viewNo,timestamp,datahash)

      this.count += 1

      val cc = new InstallCCEventMsg(selfAddress,msg.userName,msg.chainname,msg.chaincodeName,msg.chaincodeVersion,msg.chaincodeClassFullName,FileUtils.fileToBytes(jarfile))
      val prepreparemsg = new PrePrepareInstallCCMsg(view,this.count,ActorMessage.digestMessage(cc),cc)

      mediator ! Publish(ClusterManager.consensusTopicName,prepreparemsg)

//      mediator ! Publish(ClusterManager.topicName,cc)


    case msg:InvokeChainCodeMsg =>
      println(s"InvokeChainCodeMsg:$msg")
      //todo to store chaincode based on chain / user ???
      val chaincodeOpt = ChainCodeMgr.getChainCode(msg.chaincodeName)
      chaincodeOpt match {
        case Some(chaincode) =>
          val res = chaincode.invoke(msg.methodName,msg.params)
          println(s"res is $res")

          //todo for test locally
          val cchandler = chaincode.getHandler.asInstanceOf[NewCCHandlerImpl]
          val blockTrans = cchandler.getBlockTrans

          BlockMgr.addBlockTrans(msg.chainname,cchandler.getBlockTrans)
          BlockMgr.serializeBlock(msg.chainname)


        case None =>
          println(s"no chaincode found :${msg.chaincodeName}")
      }

//      mediator ! Publish(ClusterManager.topicName,new InvokeCCEventMsg(msg.userName,msg.chainname,msg.chaincodeName,msg.chaincodeVersion,msg.methodName,msg.params))
  }
}
