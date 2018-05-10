#! /bin/bash

readonly script_dir=$(cd `dirname $0` && pwd)
source "${script_dir}/config.sh"
cd "${base_dir}"

# Stop Quorum node and it's socket file
readonly qid=`ps x --format pid,command | grep -E "^\s*\w* geth --datadir data\s*--port ${quorum[port]}" | awk '{print $1}'`
#echo $qid

if [ -z ${qid} ]; then
  echo "There's no 'geth' process for ${quorum[name]}"
else
  kill -9 ${qid}
  if [ $? -ne 0 ]; then
    echo "Fail to kill 'geth' process whose PID is ${qid}"
    exit 101
  else
    echo "Sucessfully removed 'geth' process whos PID is ${qid}"
    rm -f data/geth.ipc
  fi
fi

# Stop Constellation node and it's socket file
readonly cid=`ps x --format pid,command | grep -E '^\s*\w* constellation-node constellation/tm.conf' | awk '{print $1}'`

if [ -z ${cid} ]; then
  echo "There's no 'constellation-node' process for ${quorum[name]}"
else
  kill -9 ${cid}
  if [ $? -ne 0 ]; then
    echo "Fail to kill 'constellation-node' process whose PID is ${cid}"
    exit 101
  else
    echo "Sucessfully removed 'constellation-node' process whos PID is ${cid}"
    rm -f constellation/tm.ipc
  fi
fi

if [ "$1" == '--clear' ]; then
  rm -Rf ${base_dir}/*
else
  echo ""
  echo "If you want remote all the data including block data, accounts, and logs, use '--clear' flag jus like 'stop.sh --clear'."
fi

