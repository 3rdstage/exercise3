#! /bin/bash

readonly script_dir=$(cd `dirname $0` && pwd)
source "${script_dir}/config.sh"

# Check availability of ports for Constellation node
if [ `lsof -i -P -n | grep LISTEN | grep ${constellation[port]} | wc -l` -gt 0 ]; then
  echo "The port ${constellation[port]} for Constellation is already bound by another process"
  exit 101
fi

# Move to base directory
cd "${base_dir}"

# Start Constellation node
set -u  
set -e
nohup constellation-node constellation/tm.conf >> logs/constellation.log 2>&1 &
echo ""
echo "Costellation node starting..."
echo "Check log file at '${base_dir}/logs/constellation.log'"

# Wait a moment before Constellation fully has started
sleep 3s

# TODO Check availability of ports for Quorum node

# Start Quorum node
PRIVATE_CONFIG=constellation/tm.conf
nohup geth --datadir data \
  --port ${quorum[port]} \
  --networkid ${quorum[networkid]} \
  --identity ${quorum[name]} \
  --permissioned --nodiscover --maxpeers 10 \
  --raft --raftport ${quorum[raftport]} \
  --rpc --rpcaddr ${quorum[rpcaddr]} --rpcport ${quorum[rpcport]} \
  --rpcapi admin,db,eth,debug,miner,net,shh,txpool,personal,web3,quorum \
  --rpccorsdomain “https://wallet.ethereum.org” \
  --verbosity ${quorum[verbosity]} \
  --emitcheckpoints \
  --unlock 0 --password data/passwd 2>> "logs/quorum.log" &
echo ""
echo "Quroum node starting.."
echo "Check log file at '${base_dir}/logs/quorum.log'"

