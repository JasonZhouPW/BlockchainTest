import java.security.MessageDigest

import com.wanda.blockchain.common.block._
import com.wanda.blockchain.common.db.model.{BlockTransaction, TransactionObject}
import com.wanda.blockchain.common.util.DigestUtil

import scala.reflect.ClassTag
import scala.reflect._
/**
  * Created by Zhou peiwen on 2017/6/23.
  */
object test extends App{

/*  var str = "ABCDEFG"
  var res = DigestUtil.digest(str.getBytes("UTF-8"))

  println(res)

  str = "abcdefg"
  res = DigestUtil.digest(str.getBytes("UTF-8"))

  println(res)

  str = "中文测试"
  res = DigestUtil.digest(str.getBytes("UTF-8"))

  println(res)*/

/*
  val str = """BlockHeader(number=1, previousHash=BEGIN, dataHash=FIRST_BLOCK,test(a=b))"""
  val reg = """(?<=\()(.+?)(?=\))""".r
  val list = reg.findAllIn(str).toList
  list.foreach(println)
*/

/*
  val bh = new BlockHeader(1,"abc","def")

  val tos = new TransInfo("00001","acct","0","100")

  val bts = List(new BlockTrans("000000001","管理员","test","1","invoke",List("a"),List(tos)))
  val bd = new BlockData(bts)

  val mts = List(new MetaData("abcdefg"))
  val bm = new BlockMetaData(mts)
  val block = new Block(bh,bd,bm)

  val json = BlockUtils.toJsonFormat(block)
  println(json)
  BlockFileMgr.writeBlock(block,0)

  println("read file")
  val readBlock = BlockFileMgr.readBlock(0)

  println(s"readblock:$readBlock")
  println(readBlock.blockData.trans.head.chaincodeParams)*/


//  val list = List(1,2,3,4,5,6,7)
//
//  val newlist = list.sortWith(_ > _)
//
//  println(newlist)

/*  test("a" ,"B")
  test(1,"2")

  def test[T:ClassTag](v:T,u:T) = {
    println(classTag[T].runtimeClass)
    plus(v,u)

  }

  def plus[T](a:T,b:T) = {
    println(a.toString + b.toString)
  }*/

  println(getGetterName("id"))

  def getGetterName(fieldName:String):String = {
    "get"+fieldName.toUpperCase.substring(0,1) + fieldName.substring(1,fieldName.length)
  }


}
