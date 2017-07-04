package com.wanda.blockchain.common.chaincode

import java.io.File

/**
  * Created by Zhou peiwen on 2017/7/4.
  */
object ChainCodeFacade {

  val chaincodeBase = "c:/work/tmp/chaincodetest"
  def installChainCode(chaincodeName:String,chainCodeVersion:String,chaincodePath:String) = {

    val jarFilePath = chaincodeBase + "/" + chaincodePath + "/" +  chaincodeName + s"_$chainCodeVersion.jar"
    val jarfile = new File(jarFilePath)
    if(!jarfile.exists()){
      throw new Exception(s"chaincode:$chaincodeName version:$chainCodeVersion does not exist!")
    }
  }

  def initChainCode(chaincodeName:String,params:java.util.ArrayList[String]) = {

  }

}
