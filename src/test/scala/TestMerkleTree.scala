import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.util.zip.Adler32

import com.wanda.blockchain.common.util.MerkleTree

/**
  * Created by Zhou peiwen on 2017/6/26.
  */
object TestMerkleTree extends App{

  val m4tree = construct4LeafTree
  val root = m4tree.getRoot

  val buf = ByteBuffer.allocate(8)
  buf.put(root.sig)
  buf.rewind()

  println(root.nType)
  println(buf.getLong())

  println("height:"+m4tree.getHeight)
  println("nnodes:"+m4tree.getNumberNodes)

  println("m4tree:"+m4tree)

  val lv1Node1 = root.left
  val lv1Node2 = root.right

  val lv2Node1 = lv1Node1.left
  val lv2Node2 = lv1Node1.right
  val lv2Node3 = lv1Node2.left
  val lv2Node4 = lv1Node2.right

  println(new String(lv2Node1.sig,"UTF-8"))

  val adler = new Adler32
  adler.update(lv2Node1.sig)
  adler.update(lv2Node2.sig)

  buf.clear()
  buf.put(lv1Node1.sig)
  buf.rewind()
  println("lv1Node1sig:"+buf.getLong())
  println("lv2None1 + 2 sig:"+adler.getValue)

  adler.reset()
  adler.update(lv1Node1.sig)
  adler.update(lv1Node2.sig)

  buf.clear()
  buf.put(root.sig)
  buf.rewind()

  println("lv1Node1 + 2 sig:"+adler.getValue)
  println("root sig:"+buf.getLong())




  def construct4LeafTree :MerkleTree = {
    val sigs = List("52e422506d8238ef3196b41db4c41ee0afd659b6",
                    "6d0b51991ac3806192f3cb524a5a5d73ebdaacf8",
                    "461848c8b70e5a57bd94008b2622796ec26db657",
                    "c938037dc70d107b3386a86df7fef17a9983cf53")
    new MerkleTree(sigs)


  }
}
