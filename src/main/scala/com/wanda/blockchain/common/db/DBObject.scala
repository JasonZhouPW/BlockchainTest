package com.wanda.blockchain.common.db

import com.sleepycat.je.{DatabaseEntry, Environment, Transaction}

/**
  * Created by Zhou peiwen on 2017/6/27.
  */
trait DBObject {

//  def put(key:String,value:String):Unit = {
//    val dbKey = new DatabaseEntry(key.getBytes("UTF-8"))
//    val dbVal = new DatabaseEntry(value.getBytes("UTF-8"))
//  }
//
//  def get(key:String):V

  def isClose:Boolean

  def put(value:Any,tx:Transaction):Boolean

  def get(key:String,clazz:Class[_]):Any

  def deleteByPK(key:String,clazz:Class[_],tx:Transaction):Any

  def getSecond(key:String,clazz:Class[_],skName:String):Any

  def getListBySencond(secondKey:String,clazz:Class[_],skName:String):List[Any]

  def close:Unit

  def getEnv:Environment




}
