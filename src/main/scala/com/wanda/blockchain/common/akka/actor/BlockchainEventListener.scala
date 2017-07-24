package com.wanda.blockchain.common.akka.actor

import akka.actor.Actor
import akka.actor.Actor.Receive
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.{Publish, Subscribe, SubscribeAck, Unsubscribe}
import com.wanda.blockchain.common.akka.cluster.ClusterManager

/**
  * Created by Zhou peiwen on 2017/7/20.
  */
class BlockchainEventListener extends Actor{

  val mediator =  DistributedPubSub(context.system).mediator

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

    case msg@_ => println(s"unknown message! $msg")

  }
}
