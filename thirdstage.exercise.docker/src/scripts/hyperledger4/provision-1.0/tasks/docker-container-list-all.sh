#! /bin/bash

readonly base_dir=$(cd `dirname $0` && pwd)/..
readonly config_file=${base_dir}/config.json

. ${base_dir}/tasks/hq-prepare.sh

fabric_ver=`jq -r '."fabric-version"' ${config_file}`
if [ $? -ne 0 ]; then
  echo "Fail to parse Fabric version from configuration file at ${config_file}."
  echo "Check and correct 'fabric-ver' in the configuration."
  exit 1
else
  echo "Fabric version : ${fabric_ver}"
fi

readonly host_user=`jq -r '."host-account".username' ${config_file}`
if [ $? -ne 0 ]; then
  echo "Fail to parse host user from configuration file at ${config_file}."
  echo "Check and correct 'host-account' in the configuration."
  exit 1
else
  echo "Host user : ${host_user}"
fi

readonly hosts=`jq -rc '.hosts | .[]' ${config_file}` #Don't miss '-c' option.
for host in ${hosts[@]}; do echo ${host}; done | parallel --no-notice --joblog /dev/null "
  id=\`echo {} | jq -r '.id'\`
  addr=\`echo {} | jq -r '.address'\`
  hostname=\`echo {} | jq -r '.hostname'\`

  echo ' '
  echo \"Docker containers at \${hostname}(\${addr})\"
  ssh -o StrictHostKeyChecking=no ${host_user}@\${addr} \"
    docker ps -a --format 'table {{.ID}}\\t{{.Names}}\\t{{.Image}}\\t{{.Status}}'
  \"
"