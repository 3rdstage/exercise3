#! /bin/bash

# Search transactions within specified range of ledgers using Rippled WebSocket API

# Input
declare -r addr='169.56.173.124'
declare -r port=1580
declare -r protocol='ws'
declare -r from_ledger=160001
declare -r to_ledger=200000
declare -r outfile='./transactions.csv'
declare -r verbose=false

if [ -f ${outfile} ]; then
  echo "File for output already exists : ${outfile}"
  echo "Rename or remove the file and try again."
  echo "This script would never overwrite or remove existing file."
  exit 1
else
  echo 'ledger_index, tx_order, tx_hash' > ${outfile}
fi; 

# Derived constants
declare -r url="${protocol}://${addr}:${port}/"
declare -r ledgers_cnt=$(( to_ledger - from_ledger + 1))

# Variables
declare no;
declare resp;
declare status;
declare txs_num;
declare txs;
declare tx;
declare total_txs=0;
declare filled_ledgers=0;
for (( i=${from_ledger}; i<=${to_ledger}; i++ )); do
    no=$(( i - from_ledger + 1 ))
    resp=`echo '{ "id": 1, "command": "ledger", "ledger_index": '$i', "transactions": true }' | websocat -n1 $url`
      
    if [ $? -ne 0 ]; then
      echo "Fail to execute 'ledger_index' command for ledger index of ${i}"
      # exit 1
      continue;
    fi;
    
    if [[ ${verbose} == 'true' ]]; then echo 'response: '${resp}; fi
    
    status=`echo ${resp} | jq '.status'`
    # echo ${status}
    
    if [[ ${status} != '"success"' ]]; then
      echo "Received ERROR repsonse from rippled server for ledger index of ${i}"
      exit 1
    fi; 
    
    txs_num=`echo ${resp} | jq '.result.ledger.transactions | length'`
    # echo ${txs_num}
    
    if [ ${txs_num}  -ne 0 ]; then
      total_txs=$(( total_txs + txs_num ))
      filled_ledgers=$(( filled_ledgers++ ))
      txs=`echo ${resp} | jq '.result.ledger.transactions'`
      #echo ${txs}
      for (( j=0; j < ${txs_num}; j++ )); do
        tx=`echo ${txs} | jq ".[${j}]"`
        echo "${i}, $(( j + 1 )), ${tx//\"/}" >> ${outfile} 
      done
    fi;
    echo "Ledger ${i}(${no}/${ledgers_cnt}) : ${txs_num} txs in this ledger and total ${total_txs} txs"
done

echo ''
echo "Found ${total_txs} transactions for ${ledgers_cnt} ledgers between [${from_ledger}, ${to_ledger}]"
if [ ${total_txs} -ne 0 ]; then
  echo "For detail, refer '${outfile}'"
fi;

