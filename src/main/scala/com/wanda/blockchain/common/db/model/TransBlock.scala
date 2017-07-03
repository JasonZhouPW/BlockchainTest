package com.wanda.blockchain.common.db.model

import com.sleepycat.persist.model.{Entity, PrimaryKey}

import scala.beans.BeanProperty

/**
  * Created by Zhou peiwen on 2017/6/29.
  */
@Entity
class TransactionBlock {

  @PrimaryKey
  @BeanProperty
  var txID:String = _

  @BeanProperty
  var blockHash:String = _


  override def toString = s"TransactionBlock(txID=$txID, blockHash=$blockHash)"
}
