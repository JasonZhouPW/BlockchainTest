package com.wanda.blockchain.common.db.model

import com.sleepycat.bind.tuple.{TupleBinding, TupleInput, TupleOutput}
import com.sleepycat.persist.model.{Entity, PrimaryKey}
import com.wanda.blockchain.common.block.TransInfo

import scala.beans.BeanProperty

/**
  * Created by Zhou peiwen on 2017/6/27.
  */
@Entity
class TransactionObject {

  @BeanProperty
  @PrimaryKey
  var txID:String = _

  @BeanProperty
  var key:String = _

  @BeanProperty
  var valFrom:String = _

  @BeanProperty
  var valTo:String = _

  def this(txID:String,key:String,valFrom:String,valTo :String) = {
    this
    this.txID = txID
    this.key = key
    this.valFrom = valFrom
    this.valTo = valTo
  }

  def this(transInfo:TransInfo) = {
    this
    this.txID = transInfo.txID
    this.key = transInfo.key
    this.valFrom = transInfo.valFrom
    this.valTo = transInfo.valTo
  }

  def toTransInfo:TransInfo = {
    new TransInfo(this.txID,this.key,this.valFrom,this.valTo)
  }


  override def toString = s"TransactionObject(txID=$txID,  key=$key, valFrom=$valFrom, valTo=$valTo)"
}

object TransactionObjectBinding extends TupleBinding[TransactionObject] {
  override def entryToObject(tupleInput: TupleInput): TransactionObject = {
    val txID = tupleInput.readString
    val key = tupleInput.readString
    val valFrom = tupleInput.readString
    val valTo = tupleInput.readString


    new TransactionObject(txID,key,valFrom,valTo)
  }

  override def objectToEntry(e: TransactionObject, tupleOutput: TupleOutput): Unit = {
    tupleOutput.writeString(e.txID)
    tupleOutput.writeString(e.key)
    tupleOutput.writeString(e.valFrom)
    tupleOutput.writeString(e.valTo)
  }
}