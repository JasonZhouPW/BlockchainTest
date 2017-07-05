package com.wanda.blockchain.common.db

import com.sleepycat.je.{Environment, Transaction}
import com.sleepycat.persist.EntityStore
import com.wanda.blockchain.common.block.TransInfo
import com.wanda.blockchain.common.db.model._

import scala.collection.mutable.ListBuffer
import scala.reflect.ClassTag
import scala.reflect._
/**
  * Created by Zhou peiwen on 2017/6/28.
  */
class JEDBStoreObject(store: EntityStore) extends DBObject {
  override def isClose: Boolean = false

  override def put[T:Manifest](value: T, txn: Transaction): Boolean = {
    try {
      val pk = store.getPrimaryIndex(classOf[String],implicitly[Manifest[T]].runtimeClass.asInstanceOf[Class[T]])
      pk.put(txn,value)
      true
    } catch {
      case e: Exception =>e.printStackTrace()
        false
    }

  }


  override def deleteByPK[T](key: String, clazz: Class[T],tx:Transaction): Boolean = {
    val pkIndex = store.getPrimaryIndex(classOf[String], clazz)
    pkIndex.delete(tx,key)
  }

  override def get[T](key: String, clazz: Class[T]): T = {
    val pkIndex = store.getPrimaryIndex(classOf[String], clazz)
    pkIndex.get(key)
  }


  override def getSecond[T](key: String, clazz: Class[T], skName: String): T = {
    val pkIndex = store.getPrimaryIndex(classOf[String], clazz)
    val skIndex = store.getSecondaryIndex(pkIndex, classOf[String], skName)
    skIndex.get(key)
  }


  override def getListBySencond[T](secondKey: String, clazz: Class[T], skName: String): List[T] = {
    val pkIndex = store.getPrimaryIndex(classOf[String],clazz)
    val skIndex = store.getSecondaryIndex(pkIndex,classOf[String],skName)

    val sCursor = skIndex.subIndex(secondKey).entities()
    val lb = new ListBuffer[T]
    try{
      val i = sCursor.iterator()
      while(i.hasNext){
        lb.append(i.next())
      }
      sCursor.close()
      lb.toList
    }catch{
      case e:Exception =>e.printStackTrace()
        Nil
    }

  }



  override def close: Unit = store.close()

  override def getEnv: Environment = store.getEnvironment
}
