package com.wanda.blockchain.common.akka.actor

import akka.actor.Props

/**
  * Created by Zhou peiwen on 2017/7/21.
  */
object TestMain2 extends App{
  import com.wanda.blockchain.common.akka.cluster.ClusterManager._


  startup(List("2551"))
  println(getSelfAddress.toString)

}
