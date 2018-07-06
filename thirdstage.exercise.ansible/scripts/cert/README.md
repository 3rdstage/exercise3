
# How to generate TLS certification

```
$ mkdir tls

$ cd tls

$ curl -sSL https://github.com/3rdstage/exercise3/raw/master/thirdstage.exercise.ansible/scripts/cert/get-tls-key-cert-2.sh \
  | bash -s -- \
  --subj '/C=KR/ST=Seoul/L=Seoul/O=SK C&C/OU=Lab/CN=Test TLS Server 2' \
  --filename test-tls-server-2```
```

