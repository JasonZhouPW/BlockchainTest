package com.wanda.blockchain.common.akka.actor

import java.io.File

import akka.actor.Actor
import akka.actor.Actor.Receive
import akka.cluster.Cluster
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.Publish
import com.wanda.blockchain.common.akka.cluster.ClusterManager
import com.wanda.blockchain.common.util.FileUtils

/**
  * Created by Zhou peiwen on 2017/8/23.
  */
class ConsesusActor extends Actor{

  val mediator = DistributedPubSub(context.system).mediator

  val selfAddress = Cluster(context.system).selfAddress

  val localCCDirectory = "c:/work/temp/files"

  override def receive: Receive = {
    case msg:PrePrepareInstallCCMsg =>
      println(s"received preprepare message :$msg")
      val fromAddress = msg.cc.fromAddress
      if(fromAddress.toString == selfAddress.toString){
        println("this is published by self ,ignore the consensus message!")
      }else{
        try{
          val chaincodeName = localCCDirectory + "/" + msg.cc.chaincodeName + ".jar"
          val path = FileUtils.bytesToFile(msg.cc.content,chaincodeName)
          println(s"chaincode has been saved to ${path.toRealPath()}")

          //publish success response
          val replymsg = new ReplyMsg(msg.v.viewNo,msg.v.timeStamp,selfAddress.toString,"SUCCESS")

          mediator ! Publish(ClusterManager.consensusTopicName,replymsg)
        }catch{
          case e:Exception => println(s"errors happened while preprepare message,${e.getMessage}")
            val file = new File(localCCDirectory + "/" + msg.cc.chaincodeName + ".jar")
            if(file.exists())file.delete()

            //publish failed response
            val replymsg = new ReplyMsg(msg.v.viewNo,msg.v.timeStamp,selfAddress.toString,s"FAILED:${e.getMessage}")

            mediator ! Publish(ClusterManager.consensusTopicName,replymsg)
        }
      }

  }
}
