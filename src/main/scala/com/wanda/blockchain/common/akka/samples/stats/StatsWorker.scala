package com.wanda.blockchain.common.akka.samples.stats

/**
  * Created by Zhou peiwen on 2017/7/18.
  */
import akka.actor.Actor

//#worker
class StatsWorker extends Actor {
  var cache = Map.empty[String, Int]
  def receive = {
    case word: String =>
      val length = cache.get(word) match {
        case Some(x) => x
        case None =>
          val x = word.length
          cache += (word -> x)
          x
      }

      sender() ! length
  }
}
//#worker