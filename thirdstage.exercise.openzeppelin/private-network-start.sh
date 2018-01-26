#! /bin/bash

readonly dir=$(cd `dirname $0` && pwd)

if [ -f ${dir}/data/ethereum/private/geth/chaindata/MANIFEST-000000 ]; then
  # https://github.com/ethereum/go-ethereum/wiki/Private-network
  geth --datadir ${dir}/data/ethereum/private init genesis.json
fi

if [ `geth account list --datadir ./data/ethereum/private/ | grep eab867039ce0e12e5ab3f71bce8ac296f890d735 | wc -l` -eq 0 ]; then
  geth account new --datadir ${dir}/data/ethereum/private --password ${dir}/password
fi

# https://medium.com/@solangegueiros/https-medium-com-solangegueiros-setting-up-ethereum-private-network-on-windows-a72ec59f2198
# parameter candidates : --lightkdf
geth --networkid 31 --port 30303 --rpc --rpcport 8545 --rpcapi personal,db,eth,net,web3 \
  --unlock eab867039ce0e12e5ab3f71bce8ac296f890d735 --password ${dir}/password --cache 64 --datadir "${dir}/data/ethereum/private" console
  



