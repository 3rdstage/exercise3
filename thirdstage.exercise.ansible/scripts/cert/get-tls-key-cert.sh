#! /bin/bash

# This script is expected to be launched from remote via 'curl'
# like the following
#
#  $ curl -sSL http://.../get-tls-key-cert.sh | bash -s -- --subj '/C=EN/...' --filename test-tls-server-1
# 

echo 'ver13'
readonly script_dir=$(cd `dirname $0` && pwd)
readonly url_base='https://github.com/3rdstage/exercise3/raw/master/thirdstage.exercise.ansible/scripts/cert'

curl -sSLOOOO ${url_base}/{test-ca.key,test-ca.crt,test-tls.cnf,generate-tls-artifacts.sh}
chmod 750 ${script_dir}/generate-tls-artifacts.sh

declare command=${script_dir}/generate-tls-artifacts.sh
for arg in "$@"; do
  # echo 'current arg: '${arg}
  if [ ${arg:0:1} == '-' ]; then 
    command=${command}" ${arg}"
  else
    command=${command}" '${arg}'"
  fi
done

# echo $command
eval ${command}

# After generation, removes unnecessary files
if [ -f ${script_dir}/test-ca.key ]; then 
  rm ${script_dir}/test-ca.key; 
fi
if [ -f ${script_dir}/test-tls.cnf ]; then
  rm ${script_dir}/test-tls.cnf; 
fi
if [ -f ${script_dir}/generate-tls-artifacts.sh ]; then
  rm ${script_dir}/generate-tls-artifacts.sh; 
fi



 


