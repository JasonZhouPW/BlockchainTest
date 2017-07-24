package com.wanda.blockchain.common.chaincode

import com.wanda.blockchain.common.db.DBStore

import scala.collection.mutable.{Map => mMap}
/**
  * Created by Zhou peiwen on 2017/7/24.
  */
object ChainCodeMgr {

  val ccMap:mMap[String,ChainCodeInterface] = mMap()

  def installChainCode(chaincodeName:String,ccClassFullName:String,chaincodeFilePath:String):ChainCodeInterface ={
    val loader = new NewCCLoader
    loader.loadJarFile(chaincodeFilePath)
    val chainCode = Class.forName(ccClassFullName).newInstance().asInstanceOf[ChainCodeInterface]
    ccMap.put(chaincodeName,chainCode)
    chainCode

  }

  def initialChainCode(userName:String,chainName:String,chaincodeName:String,chaincodeVersion:String) :Boolean= {
    ccMap.get(chaincodeName) match{
      case Some(chainCode) =>
        chainCode.init
        val cchandler =new NewCCHandlerImpl

        cchandler.setDbObject(DBStore.getDB(chainName))
        cchandler.init(userName,chaincodeName,chaincodeVersion)
        chainCode.setHandler(cchandler)
        true

      case None => false
    }


  }

  def getChainCode(chaincodeName:String):Option[ChainCodeInterface] = {
    ccMap.get(chaincodeName)
  }

}
