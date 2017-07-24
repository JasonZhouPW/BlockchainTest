import java.io.File

import com.wanda.blockchain.common.util.{DigestUtil, FileUtils}

/**
  * Created by Zhou peiwen on 2017/7/24.
  */
object TestFiles extends App{

  val orifile = "C:\\work\\temp\\testjar\\TestJarLoad-1.0-SNAPSHOT.jar"

  val desfile = "c:/work/temp/files/TestJarLoad.jar"

  val file = new File(orifile)

  val bytes = FileUtils.fileToBytes(file)

  val savedFile = FileUtils.bytesToFile(bytes,desfile)

  println("save done")

  val sha1 = DigestUtil.digest(bytes)
  val sha2 = DigestUtil.digest(FileUtils.fileToBytes(savedFile.toFile))

  println(s"sha1:$sha1")
  println(s"sha2:$sha2")

}
