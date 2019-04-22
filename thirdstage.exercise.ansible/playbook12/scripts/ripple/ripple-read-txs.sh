#! /bin/bash

declare addr='169.56.69.140'
declare port=5515
declare verbose=false
declare infile='blocks.csv'
declare outfile='transactions.csv'

declare counter=1;
echo 'ledger_index, time, tx_hash, tx_type' > ./${outfile}
cat ./${infile} | while read -r line; do
  if [ $counter -eq 1 ]; then
    let counter++
    continue; 
  fi

  IFS_TMP=$IFS; IFS=','; read -r -a fields <<< "${line}"; IFS=$IFS_TMP
  blk_idx=${fields[0]}
  close_time=${fields[1]:2:20}
  close_time=${close_time/Aug/08}
  close_time=`date -u +'%Y-%m-%d %H:%M:%S' -d "${close_time} +0000 + 9 hours"`
  
  tx_hash=${fields[2]//\"/}
  tx_hash=${tx_hash## }
  echo $blk_idx'; '$close_time'; '$tx_hash 

  resp=`curl -ksS --header "Content-Type: application/json" \
    --header "Connection: keep-alive" \
    --data "{\"method\": \"tx\", \"params\": [ { \"transaction\": \"${tx_hash}\", \"binary\": false } ]}" \
    http://${addr}:${port} | jq '.'`
  #echo ${resp}
  
  if [ $? -ne 0 ]; then
    echo "Fail to execute 'tx' command for tx hash of '${tx_hash}'"
    exit 1
  fi;
   
  status=`echo ${resp} | jq '.result.status'`
  #echo ${status}

  if [[ ${status} != '"success"' ]]; then
    echo "$blk_idx:$tx_hash - Received error response from Rippled"
    exit 1
  fi;

  tx_type=`echo ${resp} | jq '.result.TransactionType'`
  echo "$blk_idx:$tx_hash - $tx_type"
  echo "$blk_idx,$close_time,$tx_hash,${tx_type//\"/}" >> ./$outfile

  let counter++; if [ $counter -gt 10 ]; then break; fi;
done;