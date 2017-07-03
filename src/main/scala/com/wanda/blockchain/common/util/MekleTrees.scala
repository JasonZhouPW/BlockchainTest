package com.wanda.blockchain.common.util

import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.util
import java.util.zip.Adler32

import org.slf4j.LoggerFactory

import scala.beans.BeanProperty

/**
  * Created by Zhou peiwen on 2017/6/23.
  */
class MerkleTree {

  val logger = LoggerFactory.getLogger(this.getClass)
  private[this] val crc = new Adler32
  private[this] var leafSigs: List[String] = Nil

  @BeanProperty
  var root: Node = _
  private var depth: Int = 0
  private var nnodes: Int = 0


  def getNumberNodes = this.nnodes

  def getHeight = this.depth
  /**
    * to create a new tree from a signature list
    *
    * @param leafSignature
    */
  def this(leafSignature: List[String]) = {
    this
    constructTree(leafSignature)
  }

  /**
    * Use this when already has a tree
    *
    * @param treeRoot
    * @param numNodes
    * @param height
    * @param leafSignatures
    */
  def this(treeRoot: Node, numNodes: Int, height: Int, leafSignatures: List[String]) = {
    this
    this.root = treeRoot
    this.nnodes = numNodes
    this.depth = height
    this.leafSigs = leafSignatures
  }


  def seriallize: Array[Byte] = {
    val magicHeaderSz = MerkleTree.INT_BYTES
    val nnodesSz = MerkleTree.INT_BYTES
    val hdrSz = magicHeaderSz + nnodesSz

    val typeByteSz = 1
    val siglength = MerkleTree.INT_BYTES

    val parentSigSz = MerkleTree.LONG_BYTES
    val leafSigSz = leafSigs(0).getBytes(StandardCharsets.UTF_8).length

    val maxSigSz =  parentSigSz max leafSigSz
    val spaceForNodes = (typeByteSz + siglength + maxSigSz) * nnodes

    val cap = hdrSz + spaceForNodes
    val buf = ByteBuffer.allocate(cap)

    buf.putInt(MerkleTree.MAGIC_HDR).putInt(nnodes) //header
    serializeBreadthFirst(buf)

    val serializedTree = new Array[Byte](buf.position())
    buf.rewind()
    buf.get(serializedTree)
    serializedTree

  }

  /**
    * create a new tree from the signatures
    *
    * @param signatures
    */
  def constructTree(signatures: List[String]): Unit = {
    require(signatures.size > 1, "Need at least 2 signatures")

    this.leafSigs = signatures
    this.nnodes = signatures.size
    var parents = bottomLevel(signatures)
    this.nnodes += parents.size
    this.depth = 1

    while (parents.size > 1) {
      parents = internalLevel(parents)
      this.depth += 1
      this.nnodes += parents.size
    }

    this.root = parents(0)

  }


  /**
    * Construct the bottom part of the tree
    *
    * @param signatures
    * @return list of parent nodes
    */
  def bottomLevel(signatures: List[String]): List[Node] = {

    val leafs = signatures.map(constructLeafNode).grouped(2)
    val parents = leafs.map(l => constructInternalNode(l(0), if (l.size == 2) l(1) else null))
    parents.toList
  }


  def constructLeafNode(signature: String): Node = {
    val leaf = new Node
    leaf.nType = MerkleTree.LEAF_SIG_TYPE
    leaf.sig = signature.getBytes(StandardCharsets.UTF_8)

    leaf
  }

  def constructInternalNode(child1: Node, child2: Node): Node = {
    val parent = new Node
    parent.nType = MerkleTree.INTERNAL_SIG_TYPE

    parent.sig = if (child2 == null) child1.sig else internalHash(child1.sig, child2.sig)
    parent.left = child1
    parent.right = child2
    parent
  }

  def internalHash(leftChildSig: Array[Byte], rightChildSig: Array[Byte]): Array[Byte] = {
    crc.reset()
    crc.update(leftChildSig)
    crc.update(rightChildSig)
    Node.longToByteArray(crc.getValue)
  }

  def internalLevel(childer: List[Node]): List[Node] = {
    val parents = childer.grouped(2).map(l => constructInternalNode(l(0), if (l.size == 2) l(1) else null))
    parents.toList
  }

  /**
    * Serialization format after the header section:
    * [(nodetype:byte)(siglength:int)(signature:[]byte)]
    *
    * @param buf
    */
  def serializeBreadthFirst(buf: ByteBuffer): Unit = {
    val q = new util.ArrayDeque[Node](nnodes / 2 + 1)
    q.add(root)

    while (!q.isEmpty) {
      val nd = q.remove()
      buf.put(nd.nType).putInt(nd.sig.length).put(nd.sig)

      if (nd.left != null) q.add(nd.left)
      if (nd.right != null) q.add(nd.right)
    }
  }

}

object MerkleTree{
  val MAGIC_HDR: Int = 0xcdaace99
  val INT_BYTES: Int = 4
  val LONG_BYTES: Int = 8
  val LEAF_SIG_TYPE: Byte = 0x0
  val INTERNAL_SIG_TYPE: Byte = 0x01
}

class Node {

  var nType: Byte = _
  var sig: Array[Byte] = _
  var left: Node = _
  var right: Node = _


  private def sigAsString: String = "[" + sig.mkString(" ") + "]"


  override def toString: String = {

    val leftType = if (left != null) String.valueOf(left.nType) else "<null>"
    var rightType = if (right != null) String.valueOf(right.nType) else "<null>"
    s"MerkleTree.Node<type:$nType,sig:${sigAsString},left(type):$leftType,right(type):$rightType"
  }
}

object Node {
  def longToByteArray(value: Long): Array[Byte] = {
    List(shiftLongToByte(value, 56),
      shiftLongToByte(value, 48),
      shiftLongToByte(value, 40),
      shiftLongToByte(value, 32),
      shiftLongToByte(value, 24),
      shiftLongToByte(value, 16),
      shiftLongToByte(value, 8)).toArray
  }

  private def shiftLongToByte(value: Long, bytes: Int): Byte = (value >> bytes).toByte
}