package com.wanda.blockchain.common.akka.actor

import akka.actor.Actor
import akka.actor.Actor.Receive
import akka.cluster.Cluster
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.{Publish, Subscribe, SubscribeAck, Unsubscribe}
import com.wanda.blockchain.common.akka.cluster.ClusterManager
import com.wanda.blockchain.common.chaincode.ChainCodeMgr
import com.wanda.blockchain.common.util.FileUtils

/**
  * Created by Zhou peiwen on 2017/7/20.
  */
class BlockchainEventListener extends Actor{

  val mediator =  DistributedPubSub(context.system).mediator

  val selfAddress = Cluster(context.system).selfAddress

  val localCCDirectory = "c:/work/temp/files"

  override def preStart(): Unit = {
    mediator ! Subscribe(ClusterManager.topicName, self)
  }

  override def postStop(): Unit = {
    mediator ! Unsubscribe(ClusterManager.topicName,self)
  }



  override def receive: Receive = {

    case msg:ProposalMessage => println("Proposal message received!")
      mediator ! Publish(ClusterManager.topicName,new AckMessage(s"acknowledge messge :$msg"))

    case msg:SyncMessage => println("SyncMessage received!")

    case msg:TransactionMessage => println("TransactionMessage received!")

    case AckMessage(msg) => println(s"AckMessage received:$msg")

    case msg:SubscribeAck => println(s"got subAck:$msg")

    case msg:InstallCCEventMsg => println(s"got InstallCCEventMsg:$msg")
      //this is for the
      println(s"selfAddress is $selfAddress")
      val fromAddress = msg.fromAddress
      if(msg.fromAddress.toString == selfAddress.toString){
        //this is published by self ,do nothing..
        println("this is published by self ,do nothing..")
      }else{
        try{
          val chaincodeName = localCCDirectory + "/" + msg.chaincodeName + ".jar"
          val path = FileUtils.bytesToFile(msg.content,chaincodeName)
          println(s"chaincode has been saved to ${path.toRealPath()}")

          //todo install after Consensus
          ChainCodeMgr.installChainCode(msg.chaincodeName,msg.chaincodeClassFullName,chaincodeName)
          //currently we just initial cc after install
          //todo modify the parameters from InstallChainCodeMsg
          ChainCodeMgr.initialChainCode(msg.userName,msg.chainname,msg.chaincodeName,msg.chaincodeVersion)

          val sendmsg = new InstallCCACKMsg(selfAddress,msg.chaincodeName,0,"OK")
          println(s"Sendmsg:$sendmsg")
          mediator ! Publish(ClusterManager.topicName,sendmsg)

        }catch{
          case e:Exception => println(s"errors happen while install chaincode! ${e.getMessage}")
            mediator ! Publish(ClusterManager.topicName,new InstallCCACKMsg(selfAddress,msg.chaincodeName,1,e.getMessage))
        }
      }

    case msg:InvokeCCEventMsg=>
      //todo just for test, need to change to proposal and commit transaction
      println(s"got the InvokeCCEventMsg:$msg")
      val ccOpt = ChainCodeMgr.getChainCode(msg.chaincodeName)
      ccOpt match {
        case Some(chaincode) =>
          val res = chaincode.invoke(msg.methodName,msg.params)
          println(s"res:$res")

        case None => println(s"no chaincode found:${msg.chaincodeName}")
      }




    case msg:CreateChannelACKMsg => println(s"got CreateChannelACKMsg:$msg")
      println(s"selfAddress is $selfAddress")
      if(selfAddress.toString == msg.fromAddress.toString){
        println("this msg is from self node , do nothing ")
      }else{
        println("this msg is from other node, do some thing else")
      }

    case msg:InstallCCACKMsg => println(s"got InstallCCACKMsg:$msg")
      if(selfAddress.toString == msg.fromAddress.toString){
        println("this msg is from self node , do nothing ")
      }else{
        println("this msg is from other node, do some thing else")
      }

    case msg@_ => println(s"unknown message! $msg")

  }
}
