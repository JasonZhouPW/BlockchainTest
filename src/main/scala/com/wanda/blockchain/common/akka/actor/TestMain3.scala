package com.wanda.blockchain.common.akka.actor

import akka.actor.Props

/**
  * Created by Zhou peiwen on 2017/7/21.
  */
object TestMain3 extends App{
  import com.wanda.blockchain.common.akka.cluster.ClusterManager._

  val svrActor = system.actorOf(Props[ServiceActor],"ServiceActor")

//  svrActor ! new InstallChainCodeMsg("test",null)
}
