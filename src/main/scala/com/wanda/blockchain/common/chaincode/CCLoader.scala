package com.wanda.blockchain.common.chaincode

import java.io.{File, FilenameFilter}
import java.net.{URL, URLClassLoader}
import java.util.jar.JarFile

/**
  * Created by Zhou peiwen on 2017/7/4.
  */
class CCLoader {

  private[this] var classLoader:ClassLoader = _
  private[this] val filePrefix:String = "file:\\"

/*  def loadPath(jarPath:String):Unit = {
    try{
      val jarFiles = new File(jarPath)
      val jarFilesArr = jarFiles.listFiles(new FilenameFilter {
        override def accept(dir: File, name: String) = name.endsWith(".jar") || name.endsWith(".JAR")
      }).toList

      val jarFilePathArr = jarFilesArr.map(jar =>new URL(filePrefix + jarPath+ File.separator + jar.getName))
      this.classLoader = new URLClassLoader(jarFilePathArr.toArray,ClassLoader.getSystemClassLoader)
    }
  }*/

  def loadJar(jarName:String) :Unit= {
    require(jarName.endsWith(".jar") || jarName.endsWith(".JAR"))
    try{
      val jarfile = new JarFile(jarName)
      val em = jarfile.entries()
      this.classLoader = ClassLoader.getSystemClassLoader
      while (em.hasMoreElements){
        val jarEntry = em.nextElement()
        val clazzFile = jarEntry.getName

        if(clazzFile.toLowerCase.endsWith(".class")){
          val clazzName = clazzFile.substring(0,clazzFile.length - ".class".length).replaceAll("/",".")
          println(s"loadclass:$clazzName")
          loadClass(clazzName)
//          Class.forName(clazzName)
        }
      }
    }


  }

  def loadClass(clazzName:String):Any = {
    if(this.classLoader == null)null
    else{
      try{
//        this.classLoader.loadClass(clazzName).newInstance()
        this.classLoader.loadClass(clazzName).newInstance()

      }catch{
        case e:Exception =>println(s"can't load classname:$clazzName")
          e.printStackTrace()

      }
    }
  }

}
