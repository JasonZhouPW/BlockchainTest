package com.wanda.blockchain.common.akka.actor

/**
  * Created by Zhou peiwen on 2017/7/20.
  */

case class ProposalMessage()

case class SyncMessage()

case class TransactionMessage()

case class AckMessage(msg:String)

case class CreateChannelACKMsg(selfID:String,state:Int,message:String)

case class InstallCCACKMsg(chaincodeName:String,state:Int,message:String)

object ActorMessage {

}
