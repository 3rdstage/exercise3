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

# Write down addresses of accounts to run file
rm -f "${script_dir}/../../run/accounts"
mkdir -p "${script_dir}/../../run"
for f in data/keystore/*; do
  cat "$f" | sed -r 's/^\{"address":"(\w*)".*/\1\n/g' >> "${script_dir}/../../run/accounts"
  # TODO limit the max. number of accounts to write-down
done

if [ $? -eq 0 ]; then
  echo ""
  echo "The addresses of accounts for the quroum node to run are written down to the file '${script_dir}/../../run/accounts'"
fi

# Write down IP address to run file
# https://www.freedesktop.org/wiki/Software/systemd/PredictableNetworkInterfaceNames/
rm -f "${script_dir}/../../run/host"
ipstr=`ip address show label eth*`;
if [ -z $ipstr ]; then ipstr=`ip address show label enp*`; fi
echo $ipstr | sed -r 's/.*inet ([0-9.]*)\/.*/\1/' | tr -d '\n' >> "${script_dir}/../../run/host"

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

