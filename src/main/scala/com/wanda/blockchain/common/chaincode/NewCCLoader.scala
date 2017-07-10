package com.wanda.blockchain.common.chaincode

import java.io.File
import java.net.{URL, URLClassLoader}

/**
  * Created by Zhou peiwen on 2017/7/10.
  */
class NewCCLoader {

  def loadJarFIle(path:String) = {
    require(path.toLowerCase().endsWith(".jar"))
    val jarFile = new File(path)
    if(jarFile != null && jarFile.exists()){
      val method = classOf[URLClassLoader].getDeclaredMethod("addURL",classOf[URL])
      var accessable = method.isAccessible
      try{
        if(accessable == false)accessable = true
        val classLoader = ClassLoader.getSystemClassLoader.asInstanceOf[URLClassLoader]
        val url = jarFile.toURI.toURL
        method.setAccessible(accessable)
        method.invoke(classLoader,url)
        println(s"load jar[name=${path}]")
      }catch {
        case e:Exception =>e.printStackTrace()
      }finally {
        method.setAccessible(false)
      }
    }

  }


}
