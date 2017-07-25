package com.wanda.blockchain.common.akka.cluster

import akka.actor.{ActorRef, ActorSystem, Address, Props}
import akka.cluster.Cluster
import com.typesafe.config.ConfigFactory
import com.wanda.blockchain.common.akka.actor.{BlockchainClusterListener, BlockchainEventListener}

/**
  * Created by Zhou peiwen on 2017/7/20.
  */
object ClusterManager {

  val systemName = "ClusterSystem"
//  val systemName = "BlockchainClusterSystem"

  val topicName = "BlockProposal"

  var system:ActorSystem = _

  var cluster:Cluster = _

  def init(port:String) = {
    val config = if(port != null)
      ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).withFallback(ConfigFactory.load())
    else ConfigFactory.load()
    system = ActorSystem(systemName,config)
    system.actorOf(Props[BlockchainEventListener],"BLOCKCHAIN_EVENT_LISTENER")
    system.actorOf(Props[BlockchainClusterListener],"BLOCKCHAIN_CLUSTER_LISTENER")

    this.cluster = Cluster(system)
  }


  def getSelfAddress:Address = {
    if(cluster == null)this.cluster = Cluster(system)
    cluster.selfAddress
  }


  //only for test
  def startup(prots:List[String]) = {

    prots.foreach(port =>{
      val config =  ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).
        withFallback(ConfigFactory.load())

      val clusterSystem = ActorSystem(systemName,config)
      if(port == "2551"){
        system = clusterSystem
        this.cluster = Cluster(system)
      }
      //start actor
      clusterSystem.actorOf(Props[BlockchainEventListener],s"BLOCKCHAIN_EVENT_LISTENER_$port")

      clusterSystem.actorOf(Props[BlockchainClusterListener],s"BLOCKCHAIN_CLUSTER_LISTENER_$port")
    })
  }
}
