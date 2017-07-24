package com.wanda.blockchain.common.akka.samples.simple

import akka.actor.Actor
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
/**
  * Created by Zhou peiwen on 2017/7/12.
  */
class SimpleClusterListener extends Actor {


  val cluster = Cluster(context.system)

  override def preStart(): Unit = {
    cluster.subscribe(self,initialStateMode = InitialStateAsEvents,
      classOf[MemberEvent],classOf[UnreachableMember])
  }

  override def postStop(): Unit = cluster.unsubscribe(self)

  override def receive = {

    case MemberUp(member) =>
      println("Member is Up: {}", member.address)
    case UnreachableMember(member) =>
      println("Member detected as unreachable: {}", member)
    case MemberRemoved(member, previousStatus) =>
      println(
        "Member is Removed: {} after {}",
        member.address, previousStatus)
    case _: MemberEvent => // ignore
  }

}
