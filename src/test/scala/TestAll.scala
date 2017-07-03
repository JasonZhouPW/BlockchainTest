import com.wanda.blockchain.common.block.{BlockMetaData, BlockMgr, BlockTrans, TransInfo}

/**
  * Created by Zhou peiwen on 2017/7/3.
  */
object TestAll extends App{

  val chainName = "TESTALL"

  //init a chain
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



}
