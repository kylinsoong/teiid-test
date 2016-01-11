
To create a Private Public Key Pair, use java SDK tooks `keytool`, execute the below commands,

~~~
$ keytool -genkey -alias teiid -keyalg RSA -validity 365 -keystore ssl-example.keystore -storetype JKS
~~~

the `ssl-example.keystore` can be used as keystore based upon the newly created private key.

With the `ssl-example.keystore` created above we can extract a public key for creating a trust store via

~~~
$ keytool -export -alias teiid -keystore ssl-example.keystore -rfc -file public.cert
~~~

This creates the `public.cert` file that contains the public key based on the private key in the `ssl-example.keystore`, continue to create a TrustStore via

~~~
$ keytool -import -alias teiid -file public.cert -storetype JKS -keystore ssl-example.truststore
~~~



java -cp target/teiid-jdbc-client-1.0-SNAPSHOT.jar:target/dependency/mysql-connector-java-5.1.30.jar -Xms5096m -Xmx5096m -XX:MaxPermSize=512m -verbose:gc -Xloggc:gc.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps com.jboss.teiid.mysql.DeserializationCaculation 300000000
