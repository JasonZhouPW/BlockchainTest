package com.wanda.blockchain.common.akka.actor

import akka.actor.Address
import com.wanda.blockchain.common.block.BlockTrans
import com.wanda.blockchain.common.consensus.pbft.View
import com.wanda.blockchain.common.util.DigestUtil

/**
  * Created by Zhou peiwen on 2017/7/20.
  */

//todo all message should use other serialize method than default

case class PrePrepareInstallCCMsg(v:View,
                                  n:Long,
                                  d:String,
                                  cc:InstallCCEventMsg)

case class PrePrepareMsg(v:View,//view
                         n:Long, //message number
                         d:String, //digest of the message
                         message:BlockTrans) //transaction message

case class PrepareMsg(v:View,//view
                      n:Long,//message number
                      d:String, //digest of the message
                      i:String //node identity)
                     )

case class ReplyMsg(vNo:Long,timeStamp:Long,i:String,r:String)

case class CommitMsg(v:View,
                     n:Long,
                     d:String,
                     i:String)

case class ProposalMessage()

case class SyncMessage()

case class TransactionMessage()

case class InstallCCEventMsg(fromAddress:Address, userName:String, chainname:String, chaincodeName:String, chaincodeVersion:String, chaincodeClassFullName:String, content:Array[Byte])

case class AckMessage(msg:String)

case class CreateChannelACKMsg(fromAddress:Address,state:Int,message:String)

case class InstallCCACKMsg(fromAddress:Address,chaincodeName:String,state:Int,message:String)

case class InvokeCCEventMsg(fromAddress:Address,userName:String,chainname:String,chaincodeName:String,chaincodeVersion:String,methodName:String,params:java.util.ArrayList[String])

object ActorMessage {

  def digestMessage(msg:InstallCCEventMsg):String = {
    val bytes1 = (msg.fromAddress + msg.userName + msg.chainname + msg.chaincodeName + msg.chaincodeVersion + msg.chaincodeClassFullName).getBytes("UTF-8")
    val totalBytes = bytes1.toList ++ msg.content
    DigestUtil.digest(totalBytes.toArray)
  }
}
