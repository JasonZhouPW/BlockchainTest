package com.wanda.blockchain.common.db

import java.io.File
import java.util.concurrent.TimeUnit

import com.sleepycat.je.{Environment, EnvironmentConfig}
import com.sleepycat.persist.{EntityStore, StoreConfig}

import scala.collection.mutable.{Map => mMap}

/**
  * Created by Zhou peiwen on 2017/6/28.
  */

//using JeDB DPL
object DBStore {
  val envHome = new File("c:/work/temp/export/dbEnv2")
  val envConfig = new EnvironmentConfig
  var env: Environment = null
  var dbMap: mMap[String, DBObject] = mMap[String, DBObject]()

  private def setup = {
    envConfig.setAllowCreate(true)
    envConfig.setTransactional(true)
    envConfig.setTxnTimeout(10,TimeUnit.SECONDS)
    env = new Environment(envHome, envConfig)
  }

  def getDB(dbName: String): DBObject = {
    if (env == null) setup

    if(dbMap.get(dbName).isEmpty){
      val storeConfig = new StoreConfig
      storeConfig.setAllowCreate(true)
      storeConfig.setTransactional(true)

      val store = new EntityStore(env, dbName, storeConfig)

      val dbObj = new JEDBStoreObject(store)
      dbMap.put(dbName,dbObj)
      dbObj
    }else{
      dbMap.get(dbName).get
    }


  }

  def finalizeEnv = {
    dbMap.filter(!_._2.isClose).foreach(_._2.close)
    dbMap.clear
    env.close
    env = null
  }
}
