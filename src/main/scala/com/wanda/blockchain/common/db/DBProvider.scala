package com.wanda.blockchain.common.db

import java.io.File

import com.sleepycat.je.{Database, DatabaseConfig, Environment, EnvironmentConfig}
import scala.collection.mutable.{Map=>mMap}
/**
  * Created by Zhou peiwen on 2017/6/27.
  */
object DBProvider {

  val envHome = new File("c:/work/temp/export/dbEnv1")
  val envConfig = new EnvironmentConfig
  var env :Environment = null
  var dbMap:mMap[String, Database] = mMap[String,Database]()

  def setup():Unit = {
    envConfig.setAllowCreate(true)
    env = new Environment(envHome,envConfig)
  }

  def getDB(dbName:String):Database = {
    if (env == null) setup()

    if(dbMap.get(dbName).isEmpty){
      val dbConfig = new DatabaseConfig
      dbConfig.setAllowCreate(true)
      val db = env.openDatabase(null, dbName, dbConfig)
      dbMap.put(dbName, db)
      db
    }else{
      dbMap.get(dbName).get
    }
  }

  def closeDB(dbName:String):Unit = {
    dbMap.get(dbName) match {
      case Some(db) => db.close()
        dbMap.remove(dbName)
      case _ => println("no db name found!")
    }
  }

  def finallizeEnv = {
    println("closing db env...")
    dbMap.foreach(entry => try{ entry._2.close();dbMap.remove(entry._1)}catch{case e:Exception =>})
    env.close()
    println("closing db env done.")
  }
}
