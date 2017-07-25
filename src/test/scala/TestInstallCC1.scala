import java.io.File

import akka.actor.Props
import akka.cluster.pubsub.DistributedPubSubMediator.Publish
import com.wanda.blockchain.common.akka.actor.{InstallChainCodeMsg, ServiceActor}
import com.wanda.blockchain.common.akka.cluster.ClusterManager
import com.wanda.blockchain.common.chaincode.{ChainCodeInterface, NewCCHandlerImpl, NewCCLoader}
import com.wanda.blockchain.common.db.DBStore
import com.wanda.blockchain.common.util.FileUtils

/**
  * Created by Zhou peiwen on 2017/7/25.
  */
object TestInstallCC1 extends App{
  import com.wanda.blockchain.common.akka.cluster.ClusterManager._

  init("2552")

  Thread.sleep(5000)

  val svrActor = system.actorOf(Props[ServiceActor],"ServiceActor")
  val jarfile = "TestJarLoad-1.0-SNAPSHOT.jar"
  val directory = "c:/work/temp/testjar"
  val chainName = "MyCC"

  svrActor ! new InstallChainCodeMsg("admin",chainName,"mycc","1.0","com.test.MyCC",jarfile)

}
