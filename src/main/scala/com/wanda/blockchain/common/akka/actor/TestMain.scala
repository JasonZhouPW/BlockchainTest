package com.wanda.blockchain.common.akka.actor

import akka.actor.Props
import com.wanda.blockchain.common.akka.cluster.ClusterManager

/**
  * Created by Zhou peiwen on 2017/7/20.
  */
object TestMain extends App{
  import com.wanda.blockchain.common.akka.cluster.ClusterManager._


  init("2552")
  val svrActor = system.actorOf(Props[ServiceActor],"ServiceActor")

  for(i <- 1 to 100){
    svrActor ! new CreateChannelMsg("Test1")
    Thread.sleep(1000)
  }


}
