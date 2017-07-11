import java.util

import com.wanda.blockchain.common.block.BlockMgr
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
  loader.loadJarFIle(directory+"/"+jarfile)
//    BlockMgr.initChain(chainName)
  val chainCode = Class.forName("com.test.MyCC").newInstance().asInstanceOf[ChainCodeInterface]
  println(chainCode)

  chainCode.init
  val cchandler =new NewCCHandlerImpl

  cchandler.setDbObject(DBStore.getDB(chainName))
  cchandler.init("admin","mycc","1.0")
  chainCode.setHandler(cchandler)
  val res = chainCode.invoke("move",new util.ArrayList[String](List("001","joe","Shanghai").asJava))
  if(res != null){
    println(res.getRes.get("status"))
  }

  println("11111"+cchandler.getBlockTrans)
  BlockMgr.addBlockTrans(chainName,cchandler.getBlockTrans)

  println(s"invoke res:$res")
  val res2 = chainCode.invoke("move",new util.ArrayList[String](List("001","joe","Beijing").asJava))
  println(s"invoke res2:$res2")
  println("22222"+cchandler.getBlockTrans)

  println("serialize block...")

  BlockMgr.addBlockTrans(chainName,cchandler.getBlockTrans)
  BlockMgr.serializeBlock(chainName)
}
