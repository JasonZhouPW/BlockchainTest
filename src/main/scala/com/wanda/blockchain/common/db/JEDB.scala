package com.wanda.blockchain.common.db

/**
  * Created by Zhou peiwen on 2017/6/26.
  */
import java.io.File
import java.util.Date

import com.sleepycat.je.DatabaseException
import com.sleepycat.je.Environment
import com.sleepycat.je.EnvironmentConfig
import com.sleepycat.persist.{EntityStore, StoreConfig}
import org.slf4j.LoggerFactory

object JEDB {
  val logger = LoggerFactory.getLogger(this.getClass)
  var jeDBEnviroment:Environment = null
  var store:EntityStore = null
  def open = {
    try{
      val envConfig = new EnvironmentConfig()
      val storeConfig = new StoreConfig
      envConfig.setAllowCreate(true)
      envConfig.setTransactional(true)
      storeConfig.setAllowCreate(true)
      storeConfig.setTransactional(true)
      jeDBEnviroment = new Environment(new File("c:/work/temp/export/dbEnv1"),envConfig)
      store = new EntityStore(jeDBEnviroment,"EntityStore",storeConfig)
    }catch{
      case dbe:DatabaseException => logger.error("DB open failed!",dbe)
      case e:Exception => logger.error("Errors in DB open!",e)
    }
  }

  def close = {
    try{
      if(store != null) store.close()
      if(jeDBEnviroment != null)jeDBEnviroment.close()
    }catch{
      case dbe:DatabaseException => logger.error("DB close failed!",dbe)
      case e:Exception => logger.error("Errors in DB close!",e)
    }
  }
}
