package com.wanda.blockchain.common.chaincode

import java.io.File
import java.net.{JarURLConnection, URL, URLClassLoader}

import scala.collection.mutable.{Map => mMap}
import scala.collection.mutable.ListBuffer

/**
  * Created by Zhou peiwen on 2017/7/10.
  */
class NewCCLoader {

//  private [this] var cachedJarFiles:mMap[String,JarURLConnection] = mMap[String,JarURLConnection]()

  def loadJarFile(path:String) = {
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

        var cp = System.getProperty("java.class.path")
        if(cp != null){
          cp += File.pathSeparator + jarFile.getCanonicalPath
        }else{
          cp = jarFile.toURI.getPath
        }
        System.setProperty("java.class.path",cp)

        //cache the jar file
/*        val jarurl = new URL("jar:"+url.toExternalForm+"!/")
        val uc = jarurl.openConnection()
        println(s"uc:$uc")
        if(uc.isInstanceOf[JarURLConnection]){
          uc.setUseCaches(true)
          uc.asInstanceOf[JarURLConnection].getManifest
          cachedJarFiles.put(path,uc.asInstanceOf[JarURLConnection])
          println(s"cache jar file:$path ${cachedJarFiles}")
        }*/

      }catch {
        case e:Exception =>e.printStackTrace()
      }finally {
        method.setAccessible(false)
      }
    }

  }

/*  def unloadJarFile(path:String):Unit = {

    cachedJarFiles.get(path) match{
      case Some(uc) => uc.getJarFile.close()
        cachedJarFiles.remove(path)
      case _ => println(s"can't find jar :$path")
    }

  }*/


}
