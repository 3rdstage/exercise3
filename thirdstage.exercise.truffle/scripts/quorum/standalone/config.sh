#! /bin/bash

# Note that it would cause error to try to init geth with 'datadir' on shared folder of VirtualBox
declare -r base_dir="${HOME}/quorum/standalone"

# For more, refer https://github.com/ethereum/go-ethereum/wiki/Command-Line-Options
declare -Ar quorum=(
  [networkid]=31
  [name]='lonleynight'
  [port]=30303
  [type]='permissioned'
  [rpcaddr]='0.0.0.0'
  [rpcport]=8545
  [raftport]=50400
  [discport]=0
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


declare -Ar constellation=(
  [port]='9030'
  [verbosity]=3
)

