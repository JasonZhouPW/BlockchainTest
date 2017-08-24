package com.wanda.blockchain.common.consensus.pbft

import com.wanda.blockchain.common.akka.actor.InstallCCEventMsg
import com.wanda.blockchain.common.block.BlockTrans

/**
  * Created by Zhou peiwen on 2017/8/22.
  */


object Status {

  val PRE_PREPARE:Int = 1

  val PREPARE:Int = 2

  val COMMIT:Int = 3

}
