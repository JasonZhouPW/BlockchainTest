package com.wanda.blockchain.common.util

import java.io.File
import java.nio.file.{Files, Path, Paths}

/**
  * Created by Zhou peiwen on 2017/7/24.
  */
object FileUtils {

  def fileToBytes(file:File):Array[Byte] = {
    Files.readAllBytes(file.toPath)
  }

  def bytesToFile(bytes:Array[Byte],fileName:String) = {
    Files.write(Paths.get(fileName),bytes)
  }

}
