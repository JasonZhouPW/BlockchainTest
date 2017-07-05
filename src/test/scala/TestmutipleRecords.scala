import com.sleepycat.persist.model.{Entity, PrimaryKey, SecondaryKey}

import scala.beans.BeanProperty
import com.sleepycat.persist.model.Relationship._
import com.wanda.blockchain.common.db.DBStore
import com.wanda.blockchain.common.db.model.TestObject
/**
  * Created by Zhou peiwen on 2017/7/3.
  */
object TestmutipleRecords extends App{

  val dbstore = DBStore.getDB("TEST")
  var tx = dbstore.getEnv.beginTransaction(null,null)
  dbstore.put(new TestObject("1","abc","1",List("1","2")),tx)

/*  dbstore.put(new TestObject("2","abc","1",List("1","2")),tx)
  dbstore.put(new TestObject("3","abc","0",List("1","2")),tx)
  dbstore.put(new TestObject("4","abc","0",List("1","2")),tx)
  dbstore.put(new TestObject("5","def","0",List("1","2")),tx)
  dbstore.put(new TestObject("6","fff","0",List("1","2")),tx)
  dbstore.put(new TestObject("7","fff","0",List("1","2")),tx)*/
  tx.commit()

  val obj1 = dbstore.get("1",classOf[TestObject])
  println(obj1)
  println(obj1.id)

//  val list = dbstore.getListBySencond("abc",classOf[TestObject],"name")
//  println(list)
//
//
//  tx = dbstore.getEnv.beginTransaction(null,null)
//  dbstore.deleteByPK("1",classOf[TestObject],tx)
//  tx.commit()
//
//  println(dbstore.get("1",classOf[TestObject]))

}

