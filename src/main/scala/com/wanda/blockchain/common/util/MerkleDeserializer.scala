package com.wanda.blockchain.common.util

import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.util

import com.wanda.blockchain.common.util.MerkleTree._
import org.slf4j.LoggerFactory

/**
  * Created by Zhou peiwen on 2017/6/23.
  */
object MerkleDeserializer {
  val logger = LoggerFactory.getLogger(this.getClass)
  def deserialize(serializedTree:Array[Byte]) :MerkleTree = {
    val buf = ByteBuffer.wrap(serializedTree)

    //read header
    require(buf.getInt() == MAGIC_HDR,"serialized byte array does not start with appropriate Magic Header")
    val totnodes = buf.getInt()

    //read data
    var leafSigs :List[String] = Nil

    //read root
    val root = new Node
    root.nType = buf.get()
    require(root.nType == LEAF_SIG_TYPE,"First serialized node is a leaf")

    readNextSignature(buf,root)

    val q = new util.ArrayDeque[Node](totnodes/2 + 1)
    var curr:Node = root

    var height = 0
    var expNumNodes = 2
    var nodesThisLevel = 0
    for(i <- 1 until totnodes){
      val child:Node = new Node
      child.nType = buf.get()
      readNextSignature(buf,child)
      q.add(child)

      if(child.nType == LEAF_SIG_TYPE){
        leafSigs ::= new String(child.sig,StandardCharsets.UTF_8)
      }

      //handles incomplete tree where a node has been "promoted"
      if(signaturesEqual(child.sig,curr.sig)){
        curr.left = child
        curr = q.remove()
        expNumNodes *= 2
        nodesThisLevel = 0
        height += 1

      }else{
        nodesThisLevel += 1
        if(curr.left == null){
          curr.left = child
        }else{
          curr.right = child
          curr = q.remove()

          if(nodesThisLevel >= expNumNodes){
            expNumNodes *= 2
            nodesThisLevel = 0
            height += 1
          }
        }
      }

    }
    new MerkleTree(root,totnodes,height,leafSigs)
  }

  def readNextSignature(buf:ByteBuffer,nd:Node):Unit = {
    val sigBytes = new Array[Byte](buf.getInt())
    buf.get(sigBytes)
    nd.sig = sigBytes
  }

  def signaturesEqual(sig:Array[Byte],sig2:Array[Byte]):Boolean = {
    if(sig.length != sig2.length){
      false
    }else{
      for(i <- 0 until sig.length){
        if(sig(i) != sig2(1)){
          return false
        }
      }
      true
    }
  }

}
