#! /bin/bash

# This script is expected to be launched from remote via 'curl'
# like the following
#
#  $ curl -sSL http://.../get-tls-key-cert.sh | bash -s -- --subj '/C=EN/...' --filename test-tls-server-1
# 

readonly script_dir=$(cd `dirname $0` && pwd)
readonly url_base='https://github.com/3rdstage/exercise3/raw/master/thirdstage.exercise.ansible/scripts/cert'
declare options=

for arg in "$@"; do
  # echo 'current arg: '${arg}
  if [ ${arg:0:1} == '-' ]; then 
    options=${options}" ${arg}"
  else
    options=${options}" '${arg}'"
  fi
done

echo $options

# curl -sSLOOO ${url_base}/{test-ca.key,test-c./a.crt,test-tls.cnf}
# chmod 750 ${script_dir}/generate-tls-artifacts.sh
 
if [ -z "${options}" ]; then
  ${script_dir}/generate-tls-artifacts.sh 
else
  ${script_dir}/generate-tls-artifacts.sh ${options}
fi

## After generation, removes unnecessary files
#
#if [ -f ${script_dir}/test-ca.key ]; then
#  rm ${script_dir}/test-ca.key
#fi
#
#if [ -f ${script_dir}/test-tls.cnf ]; then
#  rm ${script_dir}/test-tls.cnf
#fi

 


