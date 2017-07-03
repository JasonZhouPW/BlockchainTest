package com.wanda.blockchain.common.db.model

import com.sleepycat.persist.model.{Entity, PrimaryKey}

import scala.beans.BeanProperty

/**
  * Created by Zhou peiwen on 2017/6/28.
  */
@Entity
class LatestBlock {

  @BeanProperty
  @PrimaryKey
  var key:String = "LATEST_BLOCK"

  @BeanProperty
  var blockNumber:Long = 0

  @BeanProperty
  var blockHash:String = _

  def this(blockNumber:Long,blockHash:String) = {
    this
    this.blockNumber = blockNumber
    this.blockHash = blockHash
  }

  override def toString = s"LatestBlock(key=$key, blockNumber=$blockNumber, blockHash=$blockHash)"
}
