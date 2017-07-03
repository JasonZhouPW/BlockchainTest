//import com.sleepycat.je.{DatabaseEntry, LockMode, Transaction}
//import com.wanda.blockchain.common.block._
//import com.wanda.blockchain.common.db.model.{BlockTransaction, TransactionObject, TransactionObjectBinding}
//import com.wanda.blockchain.common.db.DBProvider
//
///**
//  * Created by Zhou peiwen on 2017/6/27.
//  */
//object TestSaveTrans extends App{
//
//
//  val trans = List(new TransactionObject("11111111","account","0","100"),
//    new TransactionObject("11111112","account","100","200"),
//    new TransactionObject("11111113","account","200","50"))
//
//  val db = DBProvider.getDB("TestChannel")
//
//  println("before put---------------")
//  trans.foreach{ t =>
//    val theKey = new DatabaseEntry(("txID_"+t.txID).getBytes("UTF-8"))
//    val theValue = new DatabaseEntry()
//    TransactionObjectBinding.objectToEntry(t,theValue)
//    db.put(null,theKey,theValue)
//  }
//
//  println("before writing blocks-------------")
//  var lbKey = new DatabaseEntry("LATEST_BLOCK".getBytes("UTF-8"))
//  var latestBlockNo = new DatabaseEntry()
//  var res = db.get(null,lbKey,latestBlockNo,LockMode.DEFAULT)
//  println("res:"+res)
//
//  val preBlockNumber = new String(latestBlockNo.getData,"UTF-8")
//  var pbhKey = new DatabaseEntry(("BLOCK_" + new String(preBlockNumber.getBytes("UTF-8"))).getBytes("UTF-8"))
//  var preBlockHash = new DatabaseEntry()
//  db.get(null,pbhKey,preBlockHash,LockMode.DEFAULT)
//
//
//  val blockData = new BlockData(List(new BlockTransaction("000000001","admin","test","1","invoke",List("1"),trans)))
//  val blockHeader = new BlockHeader(preBlockNumber.toLong + 1,new String(preBlockHash.getData),BlockUtils.hashBlockData(blockData))
//  val blockmeta = new BlockMetaData(List(new MetaData("meta")))
//
//  //update latest block number
//  println("update latest block number")
//  db.put(null,lbKey,new DatabaseEntry((preBlockNumber.toLong + 1).toString.getBytes("UTF-8")))
//
//  println("insert block hash")
//  db.put(null,new DatabaseEntry(s"BLOCK_${(preBlockNumber.toLong + 1)}".getBytes("UTF-8")),new DatabaseEntry(BlockUtils.hashBlockData(blockData).getBytes()))
//
//  println("writing block file")
//  val block = new Block(blockHeader,blockData,blockmeta)
//
//  BlockFileMgr.writeBlock(block,preBlockNumber.toInt + 1)
//
//  println("before get----------------")
//  val queryKey = new DatabaseEntry("txID_11111112".getBytes("UTF-8"))
//  var data = new DatabaseEntry()
//  db.get(null,queryKey,data,LockMode.DEFAULT)
//
//  val ts = TransactionObjectBinding.entryToObject(data)
//  println("data is :"+ts)
//
//
//
//  DBProvider.closeDB("TestChannel")
//  DBProvider.finallizeEnv
//
//}
