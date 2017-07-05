package com.wanda.blockchain.common.db

import com.sleepycat.je.{DatabaseEntry, Environment, Transaction}

import scala.reflect.ClassTag

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

  def put[T:Manifest](value:T,tx:Transaction):Boolean

  def get[T](key:String,clazz:Class[T]):T

  def deleteByPK[T](key:String,clazz:Class[T],tx:Transaction):Boolean

  def getSecond[T](key:String,clazz:Class[T],skName:String):T

  def getListBySencond[T](secondKey:String,clazz:Class[T],skName:String):List[T]

  def close:Unit

  def getEnv:Environment




}
