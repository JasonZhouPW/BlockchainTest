package com.wanda.blockchain.common.db

import com.sleepycat.je.{Database, DatabaseEntry}

/**
  * Created by Zhou peiwen on 2017/6/27.
  */
class JEDBObject(val db:Database,val dbName:String){


//  override def put[V](key: String, value: V): Unit = {
///*    (key,value) match {
//      case (k:String,v:String) =>
//        val key = new DatabaseEntry(k.toString.getBytes("UTF-8"))
//        val value = new DatabaseEntry(v.toString.getBytes("UTF-8"))
//        db.put(null,key,value)
//      case (k:String,v:TransactionObject) =>
//        val key = new DatabaseEntry(k.getBytes("UTF-8"))
//        val value = new DatabaseEntry()
//        TransactionObjectBinding.objectToEntry(v,value)
//        db.put(null,key,value)
//      case _ => println("not support type")
//    }*/
//  }
//
//  override def get[V](key: String): V = {
//
//    val converter = implicitly[String => V]
//    val queryKey = new DatabaseEntry(key.getBytes("UTF-8"))
//
//
//
//  }
}
