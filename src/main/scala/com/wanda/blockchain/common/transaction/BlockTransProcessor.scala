package com.wanda.blockchain.common.transaction

import com.wanda.blockchain.common.block.BlockTrans
import com.wanda.blockchain.common.db.DBStore
import com.wanda.blockchain.common.db.model.{BlockInfo, BlockTransaction, TransactionObject}

/**
  * Created by Zhou peiwen on 2017/6/30.
  */
object BlockTransProcessor {

  def addNewBlockTrans(bt:BlockTrans,chainName:String):Boolean = {

    val txID = bt.transID
    val userID = bt.userID
    val chaincodeName  = bt.chaincodeName
    val chaincodeVer = bt.chaincodeVersion
    val chaincodeMethod = bt.chaincodeMethod
    val chaincodeParam = bt.chaincodeParams

    val subTrans = bt.subTrans

    val db = DBStore.getDB(chainName)
    val tx = db.getEnv.beginTransaction(null,null)
    subTrans.foreach(subTx => db.put(new TransactionObject(subTx),tx))

    val nbt = new BlockTransaction(bt)
    db.put(bt,tx)

    tx.commit()
    true
  }


}
