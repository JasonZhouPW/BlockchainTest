import com.wanda.blockchain.common.chaincode.CCLoader

/**
  * Created by Zhou peiwen on 2017/7/4.
  */
object TestJarLoad extends App{

  val directory = "c:/work/temp/testjar"

  val loader = new CCLoader
  loader.loadPath(directory)

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





//  val method = hello.getClass.getDeclaredMethod("sayHi",null)
//  println(method.invoke(hello,null))


}
