import com.wanda.blockchain.common.block.{BlockMetaData, BlockMgr, BlockTrans, TransInfo}
import com.wanda.blockchain.common.db.DBStore
import com.wanda.blockchain.common.db.model.BlockTransaction

/**
  * Created by Zhou peiwen on 2017/7/3.
  */
object TestAll extends App{

  val chainName = "TESTALL"

//  init a chain
  BlockMgr.initChain(chainName)
  println("genesis block has been settled")
  /**
    * supose we have a chaincode invoke and make some transaction
    */

  val blockTrans = new BlockTrans(transID = "000000001",
    userID = "ADMIN",
    chaincodeName = "mycc",
    chaincodeVersion = "1.0",
    chaincodeMethod = "move",
    chaincodeParams = List("a","b","100"),
    subTrans = List(new TransInfo("S0001","acct_a","1000","900"),
      new TransInfo("S0002","acct_b","0","100")))

  println(s"add block trans:$blockTrans")
  BlockMgr.addBlockTrans(chainName,blockTrans)

//  val db = DBStore.getDB(chainName)
//  val tmp = db.get("000000001",classOf[BlockTransaction])
//  println(s"tmp:$tmp")

  val blockTrans2 = new BlockTrans(transID = "000000002",
    userID = "ADMIN",
    chaincodeName = "mycc",
    chaincodeVersion = "1.0",
    chaincodeMethod = "move",
    chaincodeParams = List("a","b","300"),
    subTrans = List(new TransInfo("S0003","acct_a","900","600"),
      new TransInfo("S0004","acct_b","100","400")))

  BlockMgr.addBlockTrans(chainName,blockTrans2)

  //update blocktrans
//  val tmp2 = tmp.asInstanceOf[BlockTransaction]
//  tmp2.status = "1"
//  tmp2.inBlockHash = "abcded"
//  val tx = db.getEnv.beginTransaction(null,null)
//  db.put(tmp2,tx)
//  tx.commit()
//  println(db.get("000000001",classOf[BlockTransaction]))

  println(s"serialize block")
  BlockMgr.serializeBlock(chainName)

  println("Done")


}
