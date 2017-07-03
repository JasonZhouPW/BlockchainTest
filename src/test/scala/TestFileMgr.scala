import com.sleepycat.je.{DatabaseEntry, LockMode}
import com.wanda.blockchain.common.block._
import com.wanda.blockchain.common.db.DBProvider
import com.wanda.blockchain.common.db.model.{BlockTransaction, TransactionObject}

/**
  * Created by Zhou peiwen on 2017/6/23.
  */
object TestFileMgr extends App{

  val header = new BlockHeader(1l,"BEGIN","FIRST_BLOCK")

  val list = List(new TransInfo("0001","acct","0","100"),
    new TransInfo("0002","acct","100","200"),
    new TransInfo("0003","acct","200","100"))

  val data = new BlockData(List(new BlockTrans("001","admin","test","1","invoke",List("1"),list)))
  val metalist = List(new MetaData("FIRST_META"))
  val meta = new BlockMetaData(metalist)

  val block = new Block(header,data,meta)

  println(s"block:$block")

/*  val db = DBProvider.getDB("TestChannel")
  val key = new DatabaseEntry("LATEST_BLOCK".getBytes("UTF-8"))
  val value = new DatabaseEntry("1".getBytes("UTF-8"))
  db.put(null,key,value)

  val blockKey = new DatabaseEntry("BLOCK_1".getBytes("UTF-8"))
  val blockVal = new DatabaseEntry("FIRST_BLOCK".getBytes("UTF-8"))

  db.put(null,blockKey,blockVal)

  var getData = new DatabaseEntry()
  db.get(null,key,getData,LockMode.DEFAULT)

  println("latest block is :"+new String(getData.getData,"UTF-8"))

  val bh = new DatabaseEntry()
  db.get(null,blockKey,bh,LockMode.DEFAULT)
  println("latest block hash is:" + new String(bh.getData,"UTF-8"))*/

  BlockFileMgr.writeBlock(block,0)

/*  DBProvider.closeDB("TestChannel")
  DBProvider.finallizeEnv*/

}
