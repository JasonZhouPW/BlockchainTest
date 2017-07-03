package com.wanda.blockchain.common.util

import java.security.MessageDigest

/**
  * Created by Zhou peiwen on 2017/6/29.
  */
object DigestUtil {

  val md = MessageDigest.getInstance("SHA-256")

  val hexDigits = List('0', '1', '2', '3', '4', '5', '6', '7',
    '8', '9', 'a', 'b', 'c', 'd', 'e', 'f').toArray

  def digest(input:Array[Byte]):String = {
    md.update(input)
    bufferToHex(md.digest())
  }

  private def bufferToHex(bytes: Array[Byte]): String = bufferToHex(bytes, 0, bytes.length)

  private def bufferToHex(bytes: Array[Byte], m: Int, n: Int): String = {
    val sb = new StringBuffer(2 * n)
    val k = m + n
    (m until k).foreach(i => appendHexPair(bytes(i), sb))
    sb.toString
  }

  private def appendHexPair(bt: Byte, sb: StringBuffer) = {
    val c0 = hexDigits((bt & 0xf0) >> 4)
    val c1 = hexDigits(bt & 0xf)
    sb.append(c0)
    sb.append(c1)
  }


}
