package com.wanda.blockchain.common.chaincode

/**
  * Created by Zhou peiwen on 2017/7/4.
  */
trait ChainCodeInterface {

  def setHandler(cCHandlerInterface: CCHandlerInterface):Unit

  def getHandler:CCHandlerInterface

  def init:Unit

  def invoke(method:String,param:java.util.ArrayList[String]):CCResponse

}

class CCResponse(val res:java.util.HashMap[String,Any])
