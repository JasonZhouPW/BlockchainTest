package com.wanda.blockchain.common.akka.actor

import akka.actor.Address

/**
  * Created by Zhou peiwen on 2017/7/20.
  */

//todo all message should use other serialize method than default

case class ProposalMessage()

case class SyncMessage()

case class TransactionMessage()

case class InstallCCEventMsg(fromAddress:Address, userName:String, chainname:String, chaincodeName:String, chaincodeVersion:String, chaincodeClassFullName:String, content:Array[Byte])

case class AckMessage(msg:String)

case class CreateChannelACKMsg(fromAddress:Address,state:Int,message:String)

case class InstallCCACKMsg(fromAddress:Address,chaincodeName:String,state:Int,message:String)

case class InvokeCCEventMsg(fromAddress:Address,userName:String,chainname:String,chaincodeName:String,chaincodeVersion:String,methodName:String,params:java.util.ArrayList[String])

object ActorMessage {

}
