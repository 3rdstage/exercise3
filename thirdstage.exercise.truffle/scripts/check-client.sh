#! /bin/bash

function print_help {
  echo "Executes some basic Ethereum JSON RPC method to check the specified Ethereum client."
  echo ""
  echo "Usage:"
  echo "  $ ${0##*/}  [-h | --help | rpc_addr]"
  echo ""
  echo "If '-h' or '--help' is given, only display this help and quit."
  echo "If rpc_addr is given, execute some basic Ethereum JSON RPC "
  echo "methods to the specified address."
  echo "If no argument is given, excute the same method to local RPC"
  echo "address of 'http://127.0.0.1:8545'."
}

if [ $# -gt 1 ]; then
  echo "Too many parameters. At the most, one parameter is possible"
  echo ""
  print_help
  exit 100
fi

declare rpc_addr='http://127.0.0.1:8545'

if [ $# -eq 1 ]; then
  if [ "$1" == "-h" -o "$1" == "--help" ]; then
    print_help
    exit 0
  fi
  rpc_addr=$1
fi

# check the specified RPC address
curl -sS $rpc_addr
if [ $? -ne 0 ]; then
  echo "Fail to connect to the specified RPC adderss '$rpc_addr'"
  exit 200
fi

echo "Executing some basic JSON RPC methods to '$rpc_addr'"
echo ""

declare -ar titles=(
'the client coinbase address'
'current client version'
'the current ethereum protocol version'
'number of peers currently connected to the client'
'whether or not client is actively mining new blocks'
'a list of addresses owned by client'
'current network ID'
'the current price per gas in wei'
'the number of most recent block'
'about the genesis block'
)

declare -ar data=(
'{"jsonrpc":"2.0","method":"eth_coinbase","params":[],"id":11}'
'{"jsonrpc":"2.0","method":"web3_clientVersion","params":[],"id":21}'
'{"jsonrpc":"2.0","method":"eth_protocolVersion","params":[],"id":31}'
'{"jsonrpc":"2.0","method":"net_peerCount","params":[],"id":41}'
'{"jsonrpc":"2.0","method":"eth_mining","params":[],"id":51}'
'{"jsonrpc":"2.0","method":"eth_accounts","params":[],"id":61}'
'{"jsonrpc":"2.0","method":"net_version","params":[],"id":71}'
'{"jsonrpc":"2.0","method":"eth_gasPrice","params":[],"id":81}'
'{"jsonrpc":"2.0","method":"eth_blockNumber","params":[],"id":91}'
'{"jsonrpc":"2.0","method":"eth_getBlockByNumber","params":["0x0", false],"id":101}'
)

for (( i = 0; i < ${#titles[@]}; i++ )); do

  cmd="curl -sSX POST --data '${data[$i]}' $rpc_addr"

  echo "// ${titles[$i]}"
  echo $cmd
  eval $cmd
  echo -e "\n"
done