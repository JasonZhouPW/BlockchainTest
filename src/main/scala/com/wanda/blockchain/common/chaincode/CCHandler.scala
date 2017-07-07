package com.wanda.blockchain.common.chaincode

import com.wanda.blockchain.common.block.{BlockTrans, TransInfo}

/**
  * Created by Zhou peiwen on 2017/7/5.
  */
trait CCHandler {

  def init(userID:String,chaincodeName:String,chaincodeVersion:String):Unit

  def save[T:Manifest](entity:T):Boolean

  def queryByPK[T](key:String,clazz:Class[T]):T

  def queryBySecondaryKey[T](key:String,clazz:Class[T],propertyName:String):T

  def queryListBySecondaryKey[T](key:String,clazz:Class[T],propertyName:String):List[T]

  def deleteByPK[T](key:String,clazz:Class[T]):Boolean

  def trans : List[TransInfo]

  def start(methodName:String,params:List[String]):Unit

  def end:Unit

  def getBlockTrans:BlockTrans
}
