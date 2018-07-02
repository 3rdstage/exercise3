#! /bin/bash

# Check curl is available or not

readonly url_base='https://github.com/3rdstage/exercise3/raw/master/thirdstage.exercise.ansible/scripts/cert'

curl -sSLOOO ${url_base}/{test-ca.key,test-ca.crt,test-tls.cnf}
 
curl -sSL ${url_base}/generate-tls-artifacts.sh | bash -s