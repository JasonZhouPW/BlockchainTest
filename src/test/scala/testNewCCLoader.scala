import com.wanda.blockchain.common.chaincode.NewCCLoader

/**
  * Created by Zhou peiwen on 2017/7/10.
  */
object testNewCCLoader extends App{

  val directory = "c:/work/temp/testjar"
  //  val directory = "C:\\work\\go\\src\\github.com\\hyperledger\\BlockchainTest\\target\\classes"

  val jarName="TestJarLoad-1.0-SNAPSHOT.jar"
  val newLoader = new NewCCLoader
  newLoader.loadJarFile(directory+"/"+jarName)

  val c = Class.forName("com.test.UserInfo")



}
