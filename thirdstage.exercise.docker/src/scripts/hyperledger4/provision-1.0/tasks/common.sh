
eval_json_path_or_default(){
  local readonly num_params=3;

  if [ $# -ne ${num_params} ]; then
    echo "Illegal call more or less than 3 parameter"
    return -1
  fi

  local readonly json_str=$1;
  local readonly path=$2;
  local readonly default=$3

  printf "printf"
  echo $json_str
  
  return 0

  # listener_addr=`echo ${kafka} | jq -r '.config."listener-address"'`
  # if [ "${listener_addr}" == "null" ]; then listener_addr=${kafka_listener_addr_0}; fi

  #local readonly parsed=`echo ${json_str} | jq -r '${path}'`
}