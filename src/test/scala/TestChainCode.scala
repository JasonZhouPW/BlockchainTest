import java.util

import com.wanda.blockchain.common.chaincode.{CCLoader, ChainCodeInterface, NewCCHandlerImpl}
import com.wanda.blockchain.common.db.DBStore
import scala.collection.JavaConverters._
/**
  * Created by Zhou peiwen on 2017/7/7.
  */
object TestChainCode extends App{


  val directory = "c:/work/temp/testjar"
  val chainName = "MyCC"
  val loader = new CCLoader
  loader.loadPath(directory)
  println(s"current thread:${Thread.currentThread().getName}"+","+Thread.currentThread().getContextClassLoader)
  val chainCode = loader.loadClass("com.test.MyCC").asInstanceOf[ChainCodeInterface]
  chainCode.init
  val cchandler =new NewCCHandlerImpl

  cchandler.setDbObject(DBStore.getDB(chainName))
  cchandler.init("admin","mycc","1.0")
  chainCode.setHandler(cchandler)
  val res = chainCode.invoke("move",new util.ArrayList[String](List("001","joe","Shanghai").asJava))

  println(s"invoke res:$res")
  println(cchandler.getBlockTrans)
}
