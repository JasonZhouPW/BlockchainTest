package com.wanda.blockchain.common.block

/**
  * Created by Zhou peiwen on 2017/6/22.
  */
case class Block(blockHeader: BlockHeader,blockData: BlockData,blockMetaData: BlockMetaData) {

  def toBytes:Array[Byte] = BlockUtils.toJsonFormat(this).getBytes("UTF-8")

}

case class BlockData(val trans:List[BlockTrans]){

}

case class BlockHeader(val number:Long,val previousHash:String,val dataHash:String)

case class BlockMetaData(val metaData:List[MetaData])

case class BlockTrans(transID:String,
                      userID:String,
                      chaincodeName:String,
                      chaincodeVersion:String,
                      chaincodeMethod:String,
                      chaincodeParams:List[String],
                      subTrans:List[TransInfo],
                      timestamp:Long = System.nanoTime())

case class TransInfo(txID:String,key:String,valFrom:String,valTo :String)