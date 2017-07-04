package com.wanda.blockchain.common.db.model

import com.sleepycat.persist.model.Relationship.MANY_TO_ONE
import com.sleepycat.persist.model.{Entity, PrimaryKey, SecondaryKey}
import scala.collection.JavaConverters._
import scala.beans.BeanProperty

/**
  * Created by Zhou peiwen on 2017/7/3.
  */
@Entity
class TestObject{
  @PrimaryKey
  @BeanProperty
  var id:String = _


  @SecondaryKey(relate = MANY_TO_ONE)
  @BeanProperty
  var name:String = _

  @BeanProperty
  var status:String = _

  @BeanProperty
  var props:java.util.ArrayList[String] = _

  def this(id:String,name:String,status:String,props:List[String]) = {
    this
    this.id = id
    this.name = name
    this.status = status
    this.props = new java.util.ArrayList(props.asJava)

  }


  override def toString = s"TestObject(id=$id, name=$name, status=$status,props=$props)"
}
