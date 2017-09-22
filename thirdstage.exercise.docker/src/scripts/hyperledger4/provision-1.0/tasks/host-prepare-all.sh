#! /bin/bash

readonly base_dir=$(cd `dirname $0` && pwd)/..
readonly config_file=${base_dir}/config.json


${base_dir}/tasks/hq-prepare.sh

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
for host in ${hosts[@]}; do echo ${host}; done | parallel --no-notice --progress --bar --joblog /dev/null "
  id=\`echo {} | jq -r '.id'\`
  addr=\`echo {} | jq -r '.address'\`
  hostname=\`echo {} | jq -r '.hostname'\`

  echo "Preparing host - id: \${id}, address: \${addr}, hostname: \{$hostname}"
  ssh -o StrictHostKeyChecking=no ${host_user}@\${addr} \"mkdir -p ~/fabric/${fabric_ver}/setup/scripts\"
  scp -o StrictHostKeyChecking=no ./host-prepare.sh ${host_user}@\${addr}:~/fabric/${fabric_ver}/setup/scripts/host-prepare.sh
  if [ \$? -ne 0 ]; then
    echo "Fail to copy 'host-prepare.sh' to \${addr}."
  fi
  ssh -o StrictHostKeyChecking=no ${host_user}@\${addr} \"
    rm -r ~/~ 2>/dev/null # TODO Remove this line later
    rm ~/fabric/${fabric_ver}/setup/host-prepare.sh 2>/dev/null # TODO Remove this line later
    chmod 744 ~/fabric/${fabric_ver}/setup/scripts/host-prepare.sh
    echo 'Running host-prepare.sh for \${addr}'
    ~/fabric/${fabric_ver}/setup/scripts/host-prepare.sh
    if [ $? -ne 0 ]; then
    echo 'Fail to run host-prepare.sh for \${addr}(\${hostname})'
    else
      echo 'Successfully run host-prepare.sh for \${addr}(\${hostname})'
    fi
  \"
"

