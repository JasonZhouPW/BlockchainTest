package com.wanda.blockchain.common.db.model

import com.sleepycat.persist.model.{Entity, PrimaryKey}

import scala.beans.BeanProperty

/**
  * Created by Zhou peiwen on 2017/6/29.
  */
@Entity
class BlockChainInfo {

  @PrimaryKey
  @BeanProperty
  var chainName: String = _

  @BeanProperty
  var height: Long = 0

  @BeanProperty
  var currentBlockHash: String = _

  @BeanProperty
  var previousBlockHash: String = _

  def this(chainName:String,height:Long,currentBlockHash:String,previousBlockHash:String) = {
    this
    this.chainName = chainName
    this.height = height
    this.currentBlockHash = currentBlockHash
    this.previousBlockHash = previousBlockHash
  }


  override def toString = s"BlockChainInfo(chainName=$chainName, height=$height, currentBlockHash=$currentBlockHash, previousBlockHash=$previousBlockHash)"
}
