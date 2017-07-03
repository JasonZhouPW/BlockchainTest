package com.wanda.blockchain.common.block

import com.wanda.blockchain.common.db.DBStore
import com.wanda.blockchain.common.db.model.{BlockChainInfo, BlockInfo, BlockTransaction, LatestBlock}

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

  def addBlockTrans(bt:BlockTrans):Boolean = {
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
    val tx = dbstore.getEnv.beginTransaction(null,null)
    try{
      //get all blockTransaction with status 0

      val list = dbstore.getListBySencond("0",classOf[BlockTransaction],"status").asInstanceOf[List[BlockTransaction]]

      if(!list.isEmpty){

        //generate block based on the trans
        val block = generateBlock(chainName,list)
        val dataHash = block.blockHeader.dataHash
        val blockNum = block.blockHeader.number
        //undate the status to "1"(serialized) and update the inBlockHash

        list.foreach(bl => {
          bl.setStatus("1")
          bl.setInBlockHash(dataHash)
          dbstore.put(bl,tx)
        })

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

      }
      tx.commit()
      true
    }catch{
      case e:Exception =>
        tx.abort()
        false
    }

  }


  private def generateBlock(chainName:String,trans:List[BlockTransaction]):Block ={
    val blockData = new BlockData(trans.map(_.toBlockTrans))
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
