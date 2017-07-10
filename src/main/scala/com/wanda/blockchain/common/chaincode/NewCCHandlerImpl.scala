package com.wanda.blockchain.common.chaincode
import java.util
import java.util.UUID

import com.wanda.blockchain.common.block.{BlockTrans, TransInfo}
import com.wanda.blockchain.common.db.DBObject

import scala.beans.BeanProperty
import scala.collection.mutable.ListBuffer
import scala.collection.JavaConverters._
/**
  * Created by Zhou peiwen on 2017/7/7.
  */
class NewCCHandlerImpl extends CCHandlerInterface{

  @BeanProperty
  var dbObject:DBObject = _

  private[this] var methodName:String = _

  private[this] var params:List[String] = Nil

  private[this] var userID:String = _

  private[this] var chaincodeName:String = _

  private[this] var chaincodeVersion:String = _

  private[this] var blockTrans:BlockTrans = _

  private[this] var transID:String = _

  private[this] val transInfos:ListBuffer[TransInfo] = new ListBuffer[TransInfo]

  override def init(userID: String, chaincodeName: String, chaincodeVersion: String): Unit = {
    this.userID = userID
    this.chaincodeName = chaincodeName
    this.chaincodeVersion = chaincodeVersion
  }

  override def save[T](entity: T,clazz:Class[T]): Boolean = {
    try{
      val pk = entity.getClass.getDeclaredFields.filter(f => f.getAnnotations.exists(a => a.annotationType().toString == "interface com.sleepycat.persist.model.PrimaryKey")).head
      if(pk  == null){
        throw new Exception("no PK found in entity!")
      }
      val fieldname = pk.getName

      println(s"fieldName:$fieldname")

      val fieldtype = pk.getType
      val method =  entity.getClass.getMethod(getGetterName(fieldname))
      //      val fieldValue =
      val value = method.invoke(entity)
      val oldval = dbObject.get(value.toString,clazz)
      println(s"oldval:$oldval")
      val trans = new TransInfo(fieldname,generateSubTxID,if(oldval == null)"" else oldval.toString,entity.toString)
      transInfos.append(trans)

      val tx = dbObject.getEnv.beginTransaction(null,null)
      dbObject.put(entity,tx)(Manifest.classType(clazz))
      tx.commit()
      true
    }catch{
      case e:Exception => e.printStackTrace()
        false
    }


  }

  override def queryByPK[T](key: String, clazz: Class[T]): T = {
    dbObject.get(key,clazz)
  }

  override def queryBySecondaryKey[T](key: String, clazz: Class[T], propertyName: String): T = dbObject.getSecond(key,clazz,propertyName)

  override def queryListBySecondaryKey[T](key: String, clazz: Class[T], propertyName: String): util.ArrayList[T] = new util.ArrayList(dbObject.getListBySencond(key,clazz,propertyName).asJava)

  override def deleteByPK[T](key: String, clazz: Class[T]): Boolean = {
    val tx = dbObject.getEnv.beginTransaction(null,null)
    dbObject.deleteByPK(key,clazz,tx)
    tx.commit()
    true
  }


  override def start(methodName: String, params: util.ArrayList[String]): Unit = {
    this.methodName = methodName
    this.params = params.asScala.toList
    this.transID = generateTxID
  }

  override def end(): Unit = this.blockTrans = new BlockTrans(this.transID,
    this.userID,
    this.chaincodeName,
    this.chaincodeVersion,
    this.methodName,
    this.params,
    this.transInfos.toList)

  private def generateSubTxID:String = {
    "ST_"+UUID.randomUUID().toString.replaceAll("-","")
  }

  private def generateTxID:String = {
    "TR_"+UUID.randomUUID().toString.replaceAll("-","")
  }

  private def getGetterName(fieldName:String):String = {
    "get"+fieldName.toUpperCase.substring(0,1) + fieldName.substring(1,fieldName.length)
  }

  def getBlockTrans: BlockTrans =
    if(this.blockTrans == null) {
      new BlockTrans(this.transID,
        this.userID,
        this.chaincodeName,
        this.chaincodeVersion,
        this.methodName,
        this.params,
        this.transInfos.toList)
    }else{
      this.blockTrans
    }

}
