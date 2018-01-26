#! /bin/bash

declare -r network_id=31
declare -r port=30303
declare -r rpc_port=8545
declare -r coin_base="0x0000000000000000000000000000000000000000"
declare -r data_dir="$(cd `dirname $0` && pwd)/data/ethereum/private2"
