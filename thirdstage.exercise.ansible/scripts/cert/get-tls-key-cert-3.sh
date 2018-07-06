#! /bin/bash

# This script is expected to be launched from remote via 'curl'
# like the following
#
#  $ curl -sSL http://.../get-tls-key-cert.sh | bash -s -- --subj '/C=EN/...' --filename test-tls-server-1
# 

echo 'ver31'
readonly script_dir=$(cd `dirname $0` && pwd)
readonly url_base='https://github.com/3rdstage/exercise3/raw/master/thirdstage.exercise.ansible/scripts/cert'

curl -sSLOOO ${url_base}/{test-ca.key,test-ca.crt,test-tls.cnf}

declare -A args
for (( i = 1; i <= $#; i++ )); do
  # echo ${@:$i:1}
  args[$i]=${@:$i:1}
done
echo ${args[@]} 

curl -sSL ${url_base}/generate-tls-artifacts.sh | bash -s -- ${args[@]}

# After generation, removes unnecessary files
if [ -f ${script_dir}/test-ca.key ]; then 
  rm ${script_dir}/test-ca.key; 
fi
if [ -f ${script_dir}/test-tls.cnf ]; then
  rm ${script_dir}/test-tls.cnf; 
fi

 


