package com.wanda.blockchain.common.block

import com.wanda.blockchain.common.db.DBStore
import com.wanda.blockchain.common.db.model._

import scala.collection.JavaConverters._
/**
  * Created by Zhou peiwen on 2017/6/29.
  */
object BlockMgr {

  def initChain(chainName:String):Boolean = {
    //generate genesis block
    val blockNum = 0
    val dataHash = "GENESISBLOCK"
    val blockHeader = new BlockHeader(0,null,dataHash)
    val blockData = new BlockData(Nil)
    val blockMetaData = generateBlockMeta
    val block = new Block(blockHeader,blockData,blockMetaData)

    val dbstore = DBStore.getDB(chainName)
    val tx = dbstore.getEnv.beginTransaction(null,null)
    //1. update latest block
    val latestBlock = new LatestBlock(blockNum,block.blockHeader.dataHash)
    dbstore.put(latestBlock,tx)
    //2. save the block
    val blockInfo = new BlockInfo(dataHash,blockNum,block.toBytes)
    dbstore.put(blockInfo,tx)
    //3. update the blockchain info
    val blockChainInfo = new BlockChainInfo(chainName,blockNum,dataHash,block.blockHeader.previousHash)
    dbstore.put(blockInfo,tx)

    //write block file
    BlockFileMgr.writeBlock(block,blockNum)
    tx.commit()
    true
  }

  def addBlock(block:Block):Boolean = {


    true
  }

  def addBlockTrans(chainName:String,bt:BlockTrans):Boolean = {

    val dbStore = DBStore.getDB(chainName)
    val tx = dbStore.getEnv.beginTransaction(null,null)
    val blockTransaction = new BlockTransaction(bt)
    val subtrans = bt.subTrans.map(new TransactionObject(_))
    subtrans.foreach(dbStore.put(_,tx))
    dbStore.put(blockTransaction,tx)
    tx.commit()
    true
  }

  def getBlockByNumber(number:Int):Block= ???

  def getBlockByHash(hash:String):Block= ???

  def getBlockChainInfo(name:String):BlockChainInfo= ???

  def getLatestBlock(chainName:String):LatestBlock = {
    val dbStore = DBStore.getDB(chainName)
    val obj = dbStore.get("LATEST_BLOCK",classOf[LatestBlock])
    if(obj != null) obj.asInstanceOf[LatestBlock] else null
  }

  def serializeBlock(chainName:String):Boolean  = {
    val dbstore = DBStore.getDB(chainName)

//    try{
      //get all blockTransaction with status 0

      val list = dbstore.getListBySencond("0",classOf[BlockTransaction],"status")
      println(s"in serializeBlock list:$list")
      if(!list.isEmpty){
        val nList = list.map(_.asInstanceOf[BlockTransaction])
        //generate block based on the trans
        val block = generateBlock(chainName,nList)
        val dataHash = block.blockHeader.dataHash
        val blockNum = block.blockHeader.number
        //undate the status to "1"(serialized) and update the inBlockHash
        var tx = dbstore.getEnv.beginTransaction(null,null)
        nList.foreach(bl => {
          println(s"in loop :$bl")
          bl.setStatus("1")
          bl.setInBlockHash(dataHash)
          dbstore.put(bl,tx)
        })
        println("1")
//        tx.commit()
//        tx = dbstore.getEnv.beginTransaction(null,null)
        //1. update latest block
        val latestBlock = new LatestBlock(blockNum,block.blockHeader.dataHash)
        dbstore.put(latestBlock,tx)
        println("2")
        //2. save the block
        val blockInfo = new BlockInfo(dataHash,blockNum,block.toBytes)
        dbstore.put(blockInfo,tx)
        println("3")
        //3. update the blockchain info
        val blockChainInfo = new BlockChainInfo(chainName,blockNum,dataHash,block.blockHeader.previousHash)
        dbstore.put(blockInfo,tx)
        tx.commit()
        //write block file
        BlockFileMgr.writeBlock(block,blockNum)

      }

      true
//    }catch{
//      case e:Exception =>e.printStackTrace()
//        tx.abort()
//        false
//    }

  }


  private def generateBlockData(chainName:String,trans:List[BlockTransaction]):BlockData = {
    val transInfo = trans.map( t => new BlockTrans(transID = t.transactionID,
      userID = t.userID,
      chaincodeName = t.chaincodeName,
      chaincodeVersion = t.chaincodeVersion,
      chaincodeMethod = t.chaincodeMethod,
      chaincodeParams = t.chaincodeParams.asScala.toList,
      t.subTransIDs.asScala.toList.map(id => getTransObjectByID(chainName,id))))
    new BlockData(transInfo)
  }

  private def getTransObjectByID(chainName:String,id:String):TransInfo = {
    val dBStore = DBStore.getDB(chainName)
    val transObj = dBStore.get(id,classOf[TransactionObject])
    if(transObj != null){
      val newObject = transObj.asInstanceOf[TransactionObject]
      new TransInfo(newObject.txID,newObject.key,newObject.valFrom,newObject.valTo)
    }else{
      null
    }
  }

  private def generateBlock(chainName:String,trans:List[BlockTransaction]):Block ={
    val blockData = generateBlockData(chainName,trans)
    val blockMeta = generateBlockMeta


    val latestBlock = this.getLatestBlock(chainName)
    //get the current blocknumber
    val number = latestBlock.blockNumber + 1

    //get the previous blockhash
    val previousHash = latestBlock.blockHash
    //get the current blockdatahash
    val dataHash = BlockUtils.hashBlockData(blockData)

//    //update latestBlock move to upper level
//    val newLatestBlock = new LatestBlock(number,dataHash)



    val blockHeader = new BlockHeader(number,previousHash,dataHash)


    new Block(blockHeader,blockData,blockMeta)

  }

  //todo define the metadata
  private def generateBlockMeta = new BlockMetaData(List(new MetaData("metadata")))
}
