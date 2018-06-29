
# How to generate TLS certification

```
$ mkdir tls

$ cd tls

$ curl -sSLOO https://github.com/3rdstage/exercise3/raw/master/thirdstage.exercise.ansible/scripts/cert/test-ca.{key,crt}
 
$ curl -sSLO https://github.com/3rdstage/exercise3/raw/master/thirdstage.exercise.ansible/scripts/cert/sample-tls.cnf

$ curl -sSL https://github.com/3rdstage/exercise3/raw/master/thirdstage.exercise.ansible/scripts/cert/generate-tls-artifacts.sh | bash -s
```


