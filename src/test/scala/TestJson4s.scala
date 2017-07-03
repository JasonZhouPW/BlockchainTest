
import com.wanda.blockchain.common.block.{BlockData, BlockTrans, TransInfo}
import com.wanda.blockchain.common.db.model.{BlockTransaction, TransactionObject}
import org.json4s.jackson.Serialization.write
import org.json4s.{DefaultFormats, Formats}
import org.json4s.jackson.JsonMethods.parse
/**
  * Created by Zhou peiwen on 2017/6/30.
  */
object TestJson4s extends App{
  implicit val jsonFormats:Formats = DefaultFormats
/*  case class obj1(field1:String,field2:String)
  case class obj2(f1:String,f2:List[obj1],f3:List[String])
  case class obj3(F1:String,F2:List[obj2])

  val o = new obj2("a",List(new obj1("a","b")),List("a"))
  val json = write(o)
  println(json)

  val o2 = parse(json).extract[obj2]
  println(o2)

  val o3 = new obj3("b",List(o))
  val json2 = write(o3)

  println(json2)
  println(parse(json2).extract[obj3])*/

  val tos = new TransInfo("00001","acct","0","100")

  val bts = new BlockTrans("000000001","admin","test","1","invoke",List("a"),List(tos))
  val bd = new BlockData(List(bts))

  val json = write(bts)
  println(json)

  val data = parse(json).extract[BlockTransaction]
  println(data)
}
