package com.wanda.blockchain.common.db

import com.sleepycat.persist.{EntityStore, PrimaryIndex, SecondaryIndex}
import com.sleepycat.persist.model.{Entity, PrimaryKey, SecondaryKey}
import com.sleepycat.persist.model.Relationship._

import scala.beans.BeanProperty

/**
  * Created by Zhou peiwen on 2017/6/26.
  */
@Entity
class ExampleEntity {

  @BeanProperty
  @PrimaryKey
  var primaryKey:String = _

  @BeanProperty
  @SecondaryKey(relate = MANY_TO_ONE)
  var secondaryKey:String = _

  @BeanProperty
  var name:String = _

  @BeanProperty
  var addr:String = _

}


class SimpleDA{

  var pIdx :PrimaryIndex[String,ExampleEntity] = null
  var sIdx:SecondaryIndex[String,String,ExampleEntity] = null

  def this(store:EntityStore) = {
    this
    pIdx = store.getPrimaryIndex(classOf[String],classOf[ExampleEntity])

    sIdx = store.getSecondaryIndex(pIdx,classOf[String],"secondaryKey")
  }
}