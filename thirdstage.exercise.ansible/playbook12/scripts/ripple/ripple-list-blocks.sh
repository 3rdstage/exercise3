#! /bin/bash

declare addr='169.56.69.140'
declare port=5515

declare start=221800
declare end=236000

declare verbose=false

declare outfile='blocks.csv'
echo 'ledger_index, time(UTC0), tx_hash' > ./${outfile}

for (( i = ${start}; i <= ${end}; i++ )); do

  resp=`curl -ksS --header "Content-Type: application/json" \
    --header "Connection: keep-alive" \
    --data "{\"method\": \"ledger\", \"params\": [ { \"ledger_index\": ${i}, \"transactions\": true } ]}" \
    http://${addr}:${port} | jq '.'`

  if [ $? -ne 0 ]; then
    echo "Fail to execute 'ledger' command for ledger index of ${i}"
    exit 1
  fi;
  
  if [[ ${verbose} == 'true' ]]; then echo 'response: '${resp}; fi 

  status=`echo ${resp} | jq '.result.status'`
  #echo ${status}

  if [[ ${status} != '"success"' ]]; then
    echo "${i} : Received error response from Rippled"
    exit 1
  fi;

  close_time=`echo ${resp} | jq '.result.ledger.close_time_human'`
  pattern='2018-Aug-(28 (21|22|23))|(29 0[0-9])'
  if [[ ! ${close_time} =~ ${pattern} ]]; then
    echo "${i} : Date is not ranged: ${close_time}"
    continue
  fi; 

  txs=`echo ${resp} | jq '.result.ledger.transactions'` 
  if [[ ${verbose} == 'true' ]]; then echo 'transactions: '${txs}; fi
  
  txs_num=`echo ${txs} | jq '. | length'`

  if [ ${txs_num} -ne 0 ]; then
    echo "${i}/${end} : ${txs_num} transactions."
    for (( j = 0; j < ${txs_num}; j++ )); do
      tx=`echo ${txs} | jq ".[${j}]"`
      echo "${i}, ${close_time}, ${tx}" >> ./${outfile}
    done
  else
    echo "${i}/${end} : NO transaction."
  fi;
done;