import com.wanda.blockchain.common.chaincode.CCLoader
import com.wanda.blockchain.common.db.DBStore

/**
  * Created by Zhou peiwen on 2017/7/4.
  */
object TestJarLoad extends App{

  val directory = "c:/work/temp/testjar"
//  val directory = "C:\\work\\go\\src\\github.com\\hyperledger\\BlockchainTest\\target\\classes"

  val jarName="TestJarLoad-1.0-SNAPSHOT.jar"

  val loader = new CCLoader
//  loader.loadPath(directory)
  loader.loadJar(directory + "/"+jarName)

/*
  val hello = loader.loadClass("com.test.Hello")


  val methods = hello.getClass.getDeclaredMethods
  methods.toList.foreach(println)

  val h = hello.asInstanceOf[com.test.TestInf]

  println(h.sayHi())
  println(h.sayHello("Jordan"))

  val entity =loader.loadClass("com.test.EntityExample")

  println(entity.getClass.getAnnotations.foreach(a => println("===="+a.annotationType())))

  val fields = entity.getClass.getDeclaredFields

  println("fileds:"+fields + "length:"+fields.length)
  fields.foreach(f => println(s"----${f.getName}:"+f.getAnnotations.foreach(a => println(s"+++${a.annotationType()}"))))

  val l = fields.filter(f => f.getAnnotations.exists(a => a.annotationType().toString == "interface com.sleepycat.persist.model.PrimaryKey"))

  println(s"l:$l,${l.length}")
  println("name:"+l.head.getName)

  val ui = loader.loadClass("com.test.UserInfo")
  println(ui.getClass.getAnnotations.foreach(a => println("===="+a.annotationType())))


  val su = ClassLoader.getSystemClassLoader.loadClass("com.test.UserInfo")

//  val threadLoader = ClassLoader.getSystemClassLoader
//  val c = Class.forName("com.test.UserInfo",true,threadLoader).newInstance()
  val c = Class.forName("com.test.UserInfo").newInstance()
  println(c)

*/

//  val store = DBStore.getDB("TEST")
//  val tx = store.getEnv.beginTransaction(null,null)
//  store.put()
//
//
//  val method = hello.getClass.getDeclaredMethod("sayHi",null)
//  println(method.invoke(hello,null))


}
