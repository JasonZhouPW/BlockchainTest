package com.wanda.blockchain.common.block

import com.wanda.blockchain.common.util.DigestUtil
import org.json4s.jackson.Serialization.write
import org.json4s.{DefaultFormats, Formats}
import org.json4s.jackson.JsonMethods.parse
/**
  * Created by Zhou peiwen on 2017/6/27.
  */
object BlockUtils {

  implicit val jsonFormats:Formats = DefaultFormats

  def hashBlockData(blockData: BlockData):String = {
    DigestUtil.digest(blockData.toString.getBytes("UTF-8"))
  }

  def toJsonFormat(block:Block):String = {
    write(block)
  }

  def parseJsonToBlock(content:String):Block = {

    parse(content).extract[Block]
  }
}
