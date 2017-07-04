package com.wanda.blockchain.common.db.model

import java.util

import com.sleepycat.persist.model.{Entity, PrimaryKey, SecondaryKey}
import com.wanda.blockchain.common.block.BlockTrans
import com.sleepycat.persist.model.Relationship._

import scala.beans.BeanProperty
import scala.collection.JavaConverters._
/**
  * Created by Zhou peiwen on 2017/6/29.
  */
@Entity
class BlockTransaction {

  @PrimaryKey
  @BeanProperty
  var transactionID:String = _

  @BeanProperty
  var userID:String = _

  @BeanProperty
  var chaincodeName:String = _

  @BeanProperty
  var chaincodeVersion:String = _

  @BeanProperty
  var chaincodeMethod:String = _

  @BeanProperty
  var chaincodeParams:java.util.ArrayList[String] = _

  @BeanProperty
  var subTransIDs:java.util.ArrayList[String] = _

  @BeanProperty
  @SecondaryKey(relate = MANY_TO_ONE)
  var status:String = "0"

  @BeanProperty
  @SecondaryKey(relate = MANY_TO_ONE)
  var inBlockHash:String = _

  def this(transID:String,
           userID:String,
           chaincodeName:String,
           chaincodeVersion:String,
           chaincodeMethod:String,
           chaincodeParams:List[String],
           subTrans:List[TransactionObject]) = {
    this
    this.transactionID = transID
    this.userID = userID
    this.chaincodeName = chaincodeName
    this.chaincodeVersion = chaincodeVersion
    this.chaincodeMethod = chaincodeMethod
    this.chaincodeParams = new util.ArrayList(chaincodeParams.asJava)
    this.subTransIDs = new util.ArrayList(subTrans.map(_.txID).asJava)
  }

  def this(bts:BlockTrans) = {
    this
    this.transactionID = bts.transID
    this.userID = bts.userID
    this.chaincodeParams = new util.ArrayList(bts.chaincodeParams.asJava)
    this.chaincodeMethod = bts.chaincodeMethod
    this.chaincodeName = bts.chaincodeName
    this.chaincodeVersion =bts.chaincodeVersion
    this.subTransIDs = new util.ArrayList(bts.subTrans.map(_.txID).asJava)
  }

//  def toBlockTrans = {
//    new BlockTrans(this.transactionID,
//                  this.userID,
//      this.chaincodeName,
//      this.chaincodeVersion,
//      this.chaincodeMethod,
//      this.chaincodeParams.asScala.toList,
//      this.subTrans.asScala.toList.map(_.toTransInfo))
//  }


  override def toString = s"BlockTransaction(transactionID=$transactionID, userID=$userID, chaincodeName=$chaincodeName, chaincodeVersion=$chaincodeVersion, chaincodeMethod=$chaincodeMethod, chaincodeParams=$chaincodeParams, subTransIDs=$subTransIDs, status=$status, inBlockHash=$inBlockHash)"
}
