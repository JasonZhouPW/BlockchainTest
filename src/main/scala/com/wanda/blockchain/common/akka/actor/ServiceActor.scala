package com.wanda.blockchain.common.akka.actor

import akka.actor.Actor
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.Publish
import com.wanda.blockchain.common.akka.cluster.ClusterManager
import com.wanda.blockchain.common.chaincode.ChainCodeMgr
import com.wanda.blockchain.common.db.DBProvider
import com.wanda.blockchain.common.util.FileUtils


/**
  * Created by Zhou peiwen on 2017/7/21.
  */

case class CreateChannelMsg(dbName:String)

case class InstallChainCodeMsg(chaincodeName:String,chaincodeClassFullName:String,content:Array[Byte])

case class InvokeChainCodeMsg()

class ServiceActor extends Actor{

  val mediator = DistributedPubSub(context.system).mediator

  val localCCDirectory = "c:/work/temp/files"

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
      mediator ! Publish(ClusterManager.topicName,new CreateChannelACKMsg("0",0,"OK"))

    case msg:InstallChainCodeMsg =>
      println("InstallChainCodeMsg")
      val chaincodeName = localCCDirectory + "/" + msg.chaincodeName + ".jar"
      val path = FileUtils.bytesToFile(msg.content,chaincodeName)
      println(s"chaincode has been saved to ${path.toRealPath()}")

      //todo install after Consensus
      ChainCodeMgr.installChainCode(msg.chaincodeName,msg.chaincodeClassFullName,chaincodeName)
      sender !



    case msg:InvokeChainCodeMsg =>
      println("InvokeChainCodeMsg")
  }
}
