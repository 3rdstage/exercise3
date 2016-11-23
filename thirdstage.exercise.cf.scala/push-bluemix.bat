cd /d %~dp0
if not exist target/cf.scala-0.0.1-SNAPSHOT-jar-with-dependencies.jar (call mvn package)
bluemix api https://api.ng.bluemix.net
bluemix login -u halfface@chollian.net
cf push