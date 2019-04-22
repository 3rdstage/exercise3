#! /bin/bash

declare script_dir=`dirname -- "$0"`
script_dir=$(cd "$script_dir" && pwd)

declare config
declare output
declare conn_timeout=3

function usage(){
  echo "$ ${0/[^\/]*\//''} [config_file output_file]"
  echo ""
  echo "    config_file : input JSON file containing configuration"
  echo "    output_file : output JSON file containing inspection result"
  echo ""
}

if [ ${#@} == 0 ]; then
  config="$script_dir/../generated/ripple-network-config.json"
  output="$script_dir/../generated/ripple-network-running.json"
else
  if [ ${#@} == 2 ]; then
    config=$1
    output=$2
 else
    echo "Wrong number of argument. Check usage."
    echo ""
    usage
    exit 101
fi
fi

# TODO Sort by name
declare -r rippleds=`cat "$config" | jq -r '.rippleds'`
declare -r rippleds_cnt=`echo $rippleds | jq 'length'`
declare -r protocol=https

declare -a rippleds2
declare rippled
declare url
declare result
declare state
declare peers_connected
declare validators
for (( i = 0; i < ${rippleds_cnt}; i++ )); do
  rippled=`echo $rippleds | jq '.['$i']
    | { name: .name, type: .type, peers_defined: .peers, proxy: .proxy.frontends[]
    | select((.backend == "rippled-rpc") and (.protocol == "'$protocol'"))}'`
  # echo ${rippled} | jq '.'

  url=`echo ${rippled} | jq -r '"\(.proxy.protocol)://\(.proxy.address):\(.proxy.port)/"'`
  #echo $url

#  --cacert "$script_dir/../files/tls/test-ca.crt" \
#  --cert "$script_dir/../files/tls/test-tls-client.crt" \
#  --key "$script_dir/../files/tls/test-tls-client.key" \
#  --cert-type PEM \


  state=`curl -ksS -X POST \
  --connect-timeout $conn_timeout \
  -H 'Connection: close' \
  -H 'Content-Type: application/json' \
  -d '{"method": "server_state", "params": [{}]}' $url`

  result=`echo $state | jq -Rr 'fromjson? | .result.status'`
  if [[ $result != 'success' ]]; then
     rippleds2[$i]=$rippled
     continue
  fi

  rippled=`echo ${rippled}' '$state | jq -s '.[0] + ( .[1].result.state | {
    build_version: .build_version, complete_ledgers: .complete_ledgers,
    io_latency_ms: .io_latency_ms,
    pubkey_node: .pubkey_node, pubkey_validator: .pubkey_validator,
    server_state: .server_state, time: .time, uptime: .uptime,
    validated_ledger_seq: .validated_ledger.seq,
    validation_quorum: .validation_quorum } )'`

  peers_connected=`curl -ksS -X POST \
  --connect-timeout $conn_timeout \
  -H 'Connection: close' \
  -H 'Content-Type: application/json' \
  -d '{"method": "peers", "params": [{}]}' $url`
  # echo $peers_connected

  result=`echo $peers_connected | jq -Rr 'fromjson? | .result.status'`
  if [[ $result != 'success' ]]; then
     rippleds2[$i]=$rippled
     continue
  fi

  rippled=`echo ${rippled}' '$peers_connected | jq -s '.[0] + { peers_found : [.[1].result.peers[].public_key] }'`
  # echo ${rippled} | jq '.'

  validators=`curl -ksS -X POST \
  --connect-timeout $conn_timeout \
  -H 'Connection: close' \
  -H 'Content-Type: application/json' \
  -d '{"method": "validators", "params": [{}]}' $url`
  # echo $validators

  result=`echo $validators | jq -Rr 'fromjson? | .result.status'`
  if [[ $result != 'success' ]]; then
     rippleds2[$i]=$rippled
     continue
  fi

  rippleds2[$i]=`echo ${rippled}' '$validators | jq -s '.[0] + { validators_trusted : .[1].result.trusted_validator_keys }'`
  # echo ${rippleds2[$i]} | jq '.'
done


declare -A key_name_map
declare key
declare name
for (( i = 0; i < ${#rippleds2[@]}; i++ )); do
  key=`echo ${rippleds2[$i]} | jq '.pubkey_node'`
  name=`echo ${rippleds2[$i]} | jq '.name'`
  key_name_map[$key]=$name
done

# for k in ${!key_name_map[@]}; do echo $k' : '${key_name_map[$k]} done

declare rippled
declare peers
declare rippleds3=[]
for (( i = 0; i < ${#rippleds2[@]}; i++ )); do
  rippled=${rippleds2[$i]}
  peers=
  found=(`echo ${rippled} | jq -R 'fromjson? | .peers_found[]'`)

  for (( j = 0; j < ${#found[@]}; j++ )); do
    key=${found[$j]}
    peers=`echo $peers' [{"key": '$key', "name": '${key_name_map[$key]}'}]' | jq -s '.[0] + .[1]'`;
  done;

  if [ ! -z $found ]; then
    rippled=`echo $rippled' '$peers | jq -s '.[0] + { peers_found_named: .[1] }'`
  fi

  rippleds3=`echo $rippleds3' '$rippled | jq -s '[.[0], .[1]] | flatten'`
done

rippleds3=`echo $rippleds3 | jq 'sort_by(if .type == "validator" then 1 else 2 end, .name)'`

echo $rippleds3 | jq '.' > $output
echo "The inspection is done successfully."
echo "The results are in the file '$output'."
echo ""


