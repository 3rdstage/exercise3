#! /bin/bash

declare -r data_dir="$(cd `dirname $0` && cd ../../../ && pwd)/data/ethereum/standalone"

# declare -r network_id=31
# declare -r port=30303
# declare -r rpc_port=8545
# declare -r coin_base="0x0000000000000000000000000000000000000000"

declare -Ar node=(
  [networkid]=31
  [name]='bloodysunday'
  [port]=30303
  [rpcaddr]='0.0.0.0'
  [rpcport]=8545
  [verbosity]=4
)

declare -Ar accounts=([count]=5 [password]='user1234')

declare -ar balances=(
  100000000000000000000
  20000000000000000000
  30000000000000000000
  40000000000000000000
  50000000000000000000
)
