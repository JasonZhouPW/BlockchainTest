import java.util

import com.wanda.blockchain.common.chaincode.{CCLoader, ChainCodeInterface, NewCCHandlerImpl, NewCCLoader}
import com.wanda.blockchain.common.db.DBStore

import scala.collection.JavaConverters._
/**
  * Created by Zhou peiwen on 2017/7/7.
  */
object TestChainCode extends App{


  val directory = "c:/work/temp/testjar"
  val jarfile = "TestJarLoad-1.0-SNAPSHOT.jar"
  val chainName = "MyCC"
  val loader = new NewCCLoader
//  loader.loadPath(directory)
//  println(s"current thread:${Thread.currentThread().getName}"+","+Thread.currentThread().getContextClassLoader)
  loader.loadJarFIle(directory+"/"+jarfile)

//  val chainCode = Class.forName("com.test.MyCC").asInstanceOf[ChainCodeInterface]
  val chainCode = Class.forName("com.test.MyCC").newInstance().asInstanceOf[ChainCodeInterface]
  println(chainCode)
//  chainCode.init
//  val initMethod = chainCode.getDeclaredMethod("init")
//  initMethod.invoke(chainCode)


  chainCode.init
  val cchandler =new NewCCHandlerImpl

  cchandler.setDbObject(DBStore.getDB(chainName))
  cchandler.init("admin","mycc","1.0")
  chainCode.setHandler(cchandler)
  val res = chainCode.invoke("move",new util.ArrayList[String](List("001","joe","Shanghai").asJava))

  println(s"invoke res:$res")
  chainCode.invoke("move",new util.ArrayList[String](List("001","joe","Shenzhen").asJava))
  println(cchandler.getBlockTrans)
}
