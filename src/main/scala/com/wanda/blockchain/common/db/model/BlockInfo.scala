package com.wanda.blockchain.common.db.model

import com.sleepycat.persist.model.Relationship._
import com.sleepycat.persist.model.{Entity, PrimaryKey, SecondaryKey}

import scala.beans.BeanProperty
/**
  * Created by Zhou peiwen on 2017/6/28.
  */
@Entity
class BlockInfo {

  @BeanProperty
  @PrimaryKey
  var dataHash:String = _

  @BeanProperty
//  @SecondaryKey(relate = ONE_TO_ONE)
  var blockNumber:Long = _

  @BeanProperty
  var block:Array[Byte] = _

  def this(dataHash:String,blockNumber:Long,block:Array[Byte]) = {
    this
    this.dataHash = dataHash
    this.blockNumber = blockNumber
    this.block = block
  }

  override def toString = s"BlockInfo(dataHash=$dataHash, blockNumber=$blockNumber, block=$block)"
}
