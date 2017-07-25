import com.wanda.blockchain.common.block._
import com.wanda.blockchain.common.db.DBStore
import com.wanda.blockchain.common.db.model.{BlockInfo, BlockTransaction, LatestBlock, TransactionObject}

/**
  * Created by Zhou peiwen on 2017/6/28.
  */
object TestDBStore extends App{

  run

  def run = {

    val dbo = DBStore.getDB("MYTEST")

    val txObj1 = new TransInfo("00000001","acct","0","1000")
    val txObj2 = new TransInfo("00000002","acct","1000","2000")
    val txObj3 = new TransInfo("00000003","acct","2000","3000")
    val txObj4 = new TransInfo("00000004","acct","3000","1000")

    val env = dbo.getEnv
    var trans = env.beginTransaction(null,null)
    dbo.put(new TransactionObject(txObj1),trans)
    dbo.put(new TransactionObject(txObj2),trans)
    dbo.put(new TransactionObject(txObj3),trans)
    dbo.put(new TransactionObject(txObj4),trans)
    trans.commit()

    val to = dbo.get("00000004",classOf[TransactionObject])
    println(to.asInstanceOf[TransactionObject])
    trans = env.beginTransaction(null,null)

    val lb2 = new LatestBlock(2,"abcdef12346")
    val lb3 = new LatestBlock(3,"abcdef12347")
    val lb4 = new LatestBlock(4,"abcdef12348")


    dbo.put(lb2,trans)
    dbo.put(lb3,trans)
    dbo.put(lb4,trans)
//    trans.abort()
//    trans = env.beginTransaction(null,null)
//    dbo.put(lb.getKey,lb,trans)
    trans.commit()

    val lbl = dbo.get("LATEST_BLOCK",classOf[LatestBlock])
    println("lbl:"+lbl)


    //add blocks
    //genisis block


    val list = List(new TransInfo("0001","acct","0","100"),
      new TransInfo("0002","acct","100","200"),
      new TransInfo("0003","acct","200","100"))
    val tsList = List(new BlockTrans("000000001","admin","test","1","invoke",List("1"),list))
    val data = new BlockData(tsList)

    val datahash = BlockUtils.hashBlockData(data)
    val header = new BlockHeader(1l,"BEGIN",datahash)
    val metalist = List(new MetaData("FIRST_META"))
    val meta = new BlockMetaData(metalist)

    val block = new Block(header,data,meta)

    val bi = new BlockInfo(datahash,1,block.toBytes)

    trans = env.beginTransaction(null,null)
    val number = lbl.asInstanceOf[LatestBlock].blockNumber+1
    val lb = new LatestBlock(number,datahash)
    dbo.put(lb,trans)
    dbo.put(bi,trans)

    BlockFileMgr.writeBlock(block,number)
    trans.commit()

    val bl = dbo.get(datahash,classOf[BlockInfo])
    println(bl)

//    val bl2 = dbo.getSecond("1",classOf[BlockInfo],"blockNumber")
//    println(s"bl2:$bl2")


    val lbll = dbo.get("LATEST_BLOCK",classOf[LatestBlock])
    println("lbl:"+lbll)


  }

}
