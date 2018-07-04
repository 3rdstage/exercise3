#! /bin/bash

# Check 'openSSL' is available or not

readonly script_dir=$(cd `dirname $0` && pwd)

echo "$@"

exit 0

readonly url_base='https://github.com/3rdstage/exercise3/raw/master/thirdstage.exercise.ansible/scripts/cert'

curl -sSLOOO ${url_base}/{test-ca.key,test-ca.crt,test-tls.cnf}
 
curl -sSL ${url_base}/generate-tls-artifacts.sh | bash -s

if [ -f ${script_dir}/test-ca.key ]; then
  rm ${script_dir}/test-ca.key
fi

if [ -f ${script_dir}/test-tls.cnf ]; then
  rm ${script_dir}/test-tls.cnf
fi

 


