package com.wanda.blockchain.common.db

import com.sleepycat.je.{Environment, Transaction}
import com.sleepycat.persist.EntityStore
import com.wanda.blockchain.common.block.TransInfo
import com.wanda.blockchain.common.db.model._

import scala.collection.mutable.ListBuffer

/**
  * Created by Zhou peiwen on 2017/6/28.
  */
class JEDBStoreObject(store: EntityStore) extends DBObject {
  override def isClose: Boolean = false

  override def put(value: Any, txn: Transaction): Boolean = {
    try {
//      val txn = if (tx != null) tx.asInstanceOf[Transaction] else null
      value match {
        case s: String =>
          val pk = store.getPrimaryIndex(classOf[String], classOf[String])
          pk.put(txn, s)
          true
        case to: TransactionObject  =>
          val pk = store.getPrimaryIndex(classOf[String], classOf[TransactionObject])
          println(s"to:$to")
          pk.put(txn, to)
          true
//        case ti: TransInfo =>
//          val pk = store.getPrimaryIndex(classOf[String], classOf[TransactionObject])
//          pk.put(txn, new TransactionObject(ti))
//          true
        case lb: LatestBlock =>
          val pk = store.getPrimaryIndex(classOf[String], classOf[LatestBlock])
          pk.put(txn, lb)
          true
        case bi: BlockInfo =>
          val pk = store.getPrimaryIndex(classOf[String], classOf[BlockInfo])
          pk.put(txn, bi)
          true
        case tb: TransactionBlock =>
          val pk = store.getPrimaryIndex(classOf[String], classOf[TransactionBlock])
          pk.put(txn, tb)
          true
        case bci: BlockChainInfo =>
          val pk = store.getPrimaryIndex(classOf[String], classOf[BlockChainInfo])
          pk.put(txn, bci)
          true

        case tx:BlockTransaction =>
          val pk = store.getPrimaryIndex(classOf[String],classOf[BlockTransaction])
          println(s"tx:$tx")
          pk.put(txn,tx)
          true
          //just for test
        case test:TestObject =>
          val pk = store.getPrimaryIndex(classOf[String],classOf[TestObject])
          pk.put(txn,test)
          true

        case _ => println("unknown type")
          true
      }
    } catch {
      case e: Exception =>e.printStackTrace()
        false
    }

  }


  override def deleteByPK(key: String, clazz: Class[_],tx:Transaction): Any = {
    val pkIndex = store.getPrimaryIndex(classOf[String], clazz)
    pkIndex.delete(tx,key)
  }

  override def get(key: String, clazz: Class[_]): Any = {
    val pkIndex = store.getPrimaryIndex(classOf[String], clazz)
    pkIndex.get(key)
  }


  override def getSecond(key: String, clazz: Class[_], skName: String): Any = {
    val pkIndex = store.getPrimaryIndex(classOf[String], clazz)
    val skIndex = store.getSecondaryIndex(pkIndex, classOf[String], skName)
    skIndex.get(key)
  }


  override def getListBySencond(secondKey: String, clazz: Class[_], skName: String): List[Any] = {
    val pkIndex = store.getPrimaryIndex(classOf[String],clazz)
    val skIndex = store.getSecondaryIndex(pkIndex,classOf[String],skName)

    val sCursor = skIndex.subIndex(secondKey).entities()
    val lb = new ListBuffer[Any]
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
