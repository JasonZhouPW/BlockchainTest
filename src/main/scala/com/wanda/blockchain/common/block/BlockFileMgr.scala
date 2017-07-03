package com.wanda.blockchain.common.block

import java.io.File
import java.nio.file._

import org.slf4j.LoggerFactory
/**
  * Created by Zhou peiwen on 2017/6/22.
  */
object BlockFileMgr {

  val logger = LoggerFactory.getLogger(this.getClass)
  val rootDir = "C:/work/temp/blockchainTest"

  val blockPrefix = "block"
  //init the dir first
  init

//  def latestBlockNumber:Int = 0

  def writeBlock(block:Block,blockNumber:Long):Unit = {
    //init first
    val filePath = generateBlockFilePath(blockNumber)
    Files.write(Paths.get(filePath),block.toBytes)
  }


  def readBlock(blockNumber:Int):Block = {
    val fileName = generateBlockFilePath(blockNumber)
    val fileContent = new String(Files.readAllBytes(Paths.get(fileName)),"UTF-8")
    println("fileContent:"+fileContent)
    BlockUtils.parseJsonToBlock(fileContent)

  }

  private def init = {
//    val path = FileSystems.getDefault.getPath(rootDir)
    val dir = new File(rootDir)
    if (!dir.exists()){
      dir.mkdir()
    }

  }

  private def formatNumber(blockNumber:Long):String = {
    var s = blockNumber.toString
    for(i <- s.length to 6){
      s = "0" + s
    }
    s
  }

  def generateBlockFilePath(blockNumber:Long):String = rootDir +"/" + blockPrefix + "_" + formatNumber(blockNumber)
}
