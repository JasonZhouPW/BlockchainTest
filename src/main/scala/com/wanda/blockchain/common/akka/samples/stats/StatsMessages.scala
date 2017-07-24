package com.wanda.blockchain.common.akka.samples.stats

/**
  * Created by Zhou peiwen on 2017/7/18.
  */
final case class StatsJob(text: String)
final case class StatsResult(meanWordLength: Double)
final case class JobFailed(reason: String)

