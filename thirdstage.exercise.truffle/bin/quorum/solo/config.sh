#! /bin/bash


# Note that it would cause error to try to init geth with 'datadir' on shared folder of VirtualBox
declare -r base_dir="${HOME}/quorum/solo"

declare -Ar quorum=(
  [name]='lonleynight'
  [type]='permissioned'
  [port]=30303
  [rpcaddr]='127.0.0.1'
  [rpcport]=8545
  [raftport]=50400
  [verbosity]=4
)

declare -Ar accounts=([count]=2 [password]='user1234')

declare -Ar constellation=(
  [port]='9030'
  [verbosity]=3
)

