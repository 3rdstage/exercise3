#! /bin/bash


# Note that it would cause error to try to init geth with 'datadir' on shared folder of VirtualBox
declare -r base_dir="${HOME}/quorum/solo"

declare -Ar quorum=(
  [networkid]=1991
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

declare -Ar constellation=(
  [port]='9030'
  [verbosity]=3
)

