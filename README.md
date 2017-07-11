# BlockchainTest
A block chain test with Scala and Java

### current function:
1. Embedded JeDB as a state DB(Berkely DB Java Edition)
2. Generate block files, each file a block
3. package chain code as Jar file, and can run the chaincode as local class.


### TODO
1. Make the stand alone system to distributed system with (akka ? pbft?)
    a. use akka cluster
        http://doc.akka.io/docs/akka/current/scala/cluster-usage.html
    
2. Encrypt and signatures.
3. Make chain code run in Sandbox(update chain code,copy the database snapshots).
4. ACL(Access Control List)
5. Support more operations(range query etc.)