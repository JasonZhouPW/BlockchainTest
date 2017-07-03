package com.wanda.blockchain.common.db

/**
  * Created by Zhou peiwen on 2017/6/26.
  */
import com.wanda.blockchain.common.db.JEDB._
object TestJeDB extends App{

  run

  def run ={
    open
    val sda = new SimpleDA(store)

    val ee1 = new ExampleEntity
    val ee2 = new ExampleEntity
    val ee3 = new ExampleEntity
    val ee4 = new ExampleEntity
    val ee5 = new ExampleEntity

    ee1.setPrimaryKey("k1")
    ee1.setSecondaryKey("sk1")
    ee1.setName("name1")
    ee1.setAddr("addr1")

    ee2.setPrimaryKey("k2")
    ee2.setSecondaryKey("sk2")
    ee2.setName("name2")
    ee2.setAddr("addr2")

    ee3.setPrimaryKey("k3")
    ee3.setSecondaryKey("sk3")
    ee3.setName("name3")
    ee3.setAddr("addr3")

    ee4.setPrimaryKey("k4")
    ee4.setSecondaryKey("sk4")
    ee4.setName("name4")
    ee4.setAddr("addr4")

    ee5.setPrimaryKey("k5")
    ee5.setSecondaryKey("sk5")
    ee5.setName("name5")
    ee5.setAddr("addr5")

    sda.pIdx.put(ee1)
    sda.pIdx.put(ee2)
    sda.pIdx.put(ee3)
    sda.pIdx.put(ee4)
    sda.pIdx.put(ee5)

    println("k1:"+sda.pIdx.get("k1").getPrimaryKey)
    println("k2:" + sda.sIdx.get("sk2").getName() )

    close

  }

}
