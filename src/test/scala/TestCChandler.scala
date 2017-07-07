import com.sleepycat.persist.model.{Entity, PrimaryKey}
import com.wanda.blockchain.common.chaincode.{CCHandlerImpl, NewCCHandlerImpl}
import com.wanda.blockchain.common.db.DBStore
import scala.collection.JavaConverters._
import scala.beans.BeanProperty
/**
  * Created by Zhou peiwen on 2017/7/6.
  */
object TestCChandler extends App{

  val chainName="testcc"
  val cch = new NewCCHandlerImpl
//  val cch = new CCHandlerImpl

  cch.init("admin","mycc","1.0")
  cch.start("move",new java.util.ArrayList(List("a","b","100").asJava))
//  cch.start("move",List("a","b","100"))
  cch.setDbObject(DBStore.getDB(chainName))

  //delete first
  val del = cch.deleteByPK("1",classOf[testobj])
  println(del)
  val t = new testobj
  t.setObjid("1")
  t.setName("jacky")
  t.setCity("shanghai")

//  cch.init("trans",List("a","b"))
  cch.save(t,classOf[testobj])

  val queryobj = cch.queryByPK("1",classOf[testobj])

  println(queryobj)

  t.setCity("Beijing")

//  cch.save(t,classOf[testobj])
  cch.save(t,classOf[testobj])

  t.setCity("Shenzhen")

//  cch.save(t,classOf[testobj])
  cch.save(t,classOf[testobj])

  cch.end


//  println(cch.trans)
  println(cch.getBlockTrans)
}

@Entity
class testobj{

  @PrimaryKey
  @BeanProperty
  var objid:String = _

  @BeanProperty
  var name:String = _

  @BeanProperty
  var city:String = _


  override def toString = s"testobj(objid=$objid, name=$name, city=$city)"
}