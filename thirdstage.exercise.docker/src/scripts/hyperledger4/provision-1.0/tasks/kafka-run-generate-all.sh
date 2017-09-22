#! /bin/bash

# References
#   - Kafka 0.9.0 documentation : https://kafka.apache.org/090/documentation.html
#   - Kafka 0.9.0 config : https://kafka.apache.org/090/documentation.html#configuration
#   - kafka-docker 0.9.0.1 : https://github.com/wurstmeister/kafka-docker/tree/0.9.0.1
#   - hyperledger/fabric-kafka : https://github.com/hyperledger/fabric/tree/v1.0.1/images/kafka

readonly base_dir=$(cd `dirname $0` && pwd)/..
readonly config_file=${base_dir}/config.json

. ${base_dir}/tasks/hq-prepare.sh

# @TODO Warn that the Kafka launcher scripts under 'generated/scripts' directory would be overwritten or removed.
# @TODO Remove or move the old launcher scripts.

# Remove previous scripts
rm -f ${base_dir}/generated/bin/kafka-run*
if [ $? -ne 0 ]; then
  echo "Fail to remove previously generated Kafka launch scripts."
  exit 1
fi

readonly host_arch=`jq -r '."host-arch"' ${config_file}`
if [ $? -ne 0 ]; then
  echo "Fail to parse host architecture from configuration file at ${config_file}."
  echo "Check and correct 'host-arch' in the configuration."
  exit 1
else
  echo "Host architecture : ${host_arch}"
fi

readonly fabric_ver=`jq -r '."fabric-version"' ${config_file}`
if [ $? -ne 0 ]; then
  echo "Fail to parse Fabric version from configuration file at ${config_file}."
  echo "Check and correct 'fabric-ver' in the configuration."
  exit 1
else
  echo "Fabric version : ${fabric_ver}"
fi

readonly host_user=`jq -r '."host-account".username' ${config_file}`
if [ $? -ne 0 ]; then
  echo "Fail to parse host user from configuration file at ${config_file}."
  echo "Check and correct 'host-account' in the configuration."
  exit 1
else
  echo "Host user : ${host_user}"
fi

readonly kafkas=`jq -rc '.kafkas | .[]' ${config_file}`
if [[ -z ${kafkas} ]]; then
  echo "Kafka instances are not defined or defined illegally in ${config_file}."
  echo "Check and correct the configuration."
  exit 1
else
  echo "Found Kafka configurations : ${kafkas}"
  echo "Generating scripts to launch Kafka instances."
fi
# readonly kafka_names=`echo ${kafkas} | jq -rc '.name'`

readonly kafka_listener_addr_0=`jq -r '."kafka-config-default"."listener-address"' ${config_file}`
readonly kafka_listener_port_0=`jq -r '."kafka-config-default"."listener-port"' ${config_file}`
readonly kafka_jmx_port_0=`jq -r '."kafka-config-default"."jmx-port"' ${config_file}`
readonly kafka_unclean_leader_election_enable_0=`jq -r '."kafka-config-default"."unclean-leader-election-enable"' ${config_file}`
readonly kafka_min_insync_replicas_0=`jq -r '."kafka-config-default"."min-insync-replicas"' ${config_file}`
readonly kafka_default_replication_factor_0=`jq -r '."kafka-config-default"."default-replication-factor"' ${config_file}`
readonly kafka_message_max_bytes_0=`jq -r '."kafka-config-default"."max-message-bytes"' ${config_file}`
readonly kafka_replica_fetch_max_bytes_0=`jq -r '."kafka-config-default"."replica-fetch-max-bytes"' ${config_file}`
readonly kafka_zk_conn_timeout_ms_0=`jq -r '."kafka-config-default"."zookeeper-connection-timeout-ms"' ${config_file}`
readonly kafka_verbose_0=`jq -r '."kafka-config-default"."verbose"' ${config_file}`
readonly kafka_log4j_rootlogger_0=`jq -r '."kafka-config-default"."log4j-rootlogger"' ${config_file}`
readonly kafka_metrics_recording_level_0=`jq -r '."kafka-config-default"."metrics-recording-level"' ${config_file}`
readonly kafka_jvm_heap_opts_0=`jq '."kafka-config-default"."jvm-opts"."heap-opts"' ${config_file}`
readonly kafka_jvm_perf_opts_0=`jq '."kafka-config-default"."jvm-opts"."performance-opts"' ${config_file}`
readonly kafka_jvm_jmx_opts_0=`jq '."kafka-config-default"."jvm-opts"."jmx-opts"' ${config_file}`
readonly kafka_jvm_gc_log_opts_0=`jq '."kafka-config-default"."jvm-opts"."gc-log-opts"' ${config_file}`
# Generate launch script for each Kafka instance
for kafka in ${kafkas[@]}; do
  name=`echo $kafka | jq -r '.name'`
  broker_id=`echo $zk | jq -r '."broker-id"'`

  listener_addr=`echo ${kafka} | jq -r '.config."listener-address"'`
  if [ "${listener_addr}" == "null" ]; then listener_addr=${kafka_listener_addr_0}; fi
  listener_port=`echo ${kafka} | jq -r '.config."listener-port"'`
  jmx_port=`echo ${kafka} | jq -r '.config."jmx-port"'`
  if [ "${jmx_port}" == "null" ]; then jmx_port=${kafka_jmx_port_0}; fi
  if [ "${listener_port}" == "null" ]; then listener_port=${kafka_listener_port_0}; fi
  unclean_leader_election_enable=`echo ${kafka} | jq -r '.config."unclean-leader-election-enable"'`
  if [ "${unclean_leader_election_enable}" == "null" ]; then unclean_leader_election_enable=${kafka_unclean_leader_election_enable_0}; fi
  min_insync_replicas=`echo ${kafka} | jq -r '.config."min-insync-replicas"'`
  if [ "${min_insync_replicas}" == "null" ]; then min_insync_replicas=${kafka_min_insync_replicas_0}; fi
  default_replication_factor=`echo ${kafka} | jq -r '.config."default-replication-factor"'`
  if [ "${default_replication_factor}" == "null" ]; then default_replication_factor=${kafka_default_replication_factor_0}; fi
  message_max_bytes=`echo ${kafka} | jq -r '.config."message_max_bytes"'`
  if [ "${message_max_bytes}" == "null" ]; then message_max_bytes=${kafka_message_max_bytes_0}; fi
  replica_fetch_max_bytes=`echo ${kafka} | jq -r '.config."replica_fetch_max_bytes"'`
  if [ "${replica_fetch_max_bytes}" == "null" ]; then replica_fetch_max_bytes=${kafka_replica_fetch_max_bytes_0}; fi
  zk_conn_timeout_ms=`echo ${kafka} | jq -r '.config."zookeeper-connection-timeout-ms"'`
  if [ "${zk_conn_timeout_ms}" == "null" ]; then zk_conn_timeout_ms=${kafka_zk_conn_timeout_ms_0}; fi
  verbose=`echo ${kafka} | jq -r '.config."verbose"'`
  if [ "${verbose}" == "null" ]; then verbose=${kafka_verbose_0}; fi
  log4j_rootlogger=`echo ${kafka} | jq -r '.config."log4j-rootlogger"'`
  if [ "${log4j_rootlogger}" == "null" ]; then log4j_rootlogger=${kafka_log4j_rootlogger_0}; fi
  metrics_recording_level=`echo ${kafka} | jq -r '.config."metrics-recording-level"'`
  if [ "${metrics_recording_level}" == "null" ]; then metrics_recording_level=${kafka_metrics_recording_level_0}; fi
  jvm_heap_opts=`echo ${kafka} | jq '.config."jvm-opts"."heap-opts"'`
  if [ "${jvm_heap_opts}" == "null" ]; then jvm_heap_opts=${kafka_jvm_heap_opts_0}; fi
  jvm_perf_opts=`echo ${kafka} | jq '.config."jvm-opts"."performance-opts"'`
  if [ "${jvm_perf_opts}" == "null" ]; then jvm_perf_opts=${kafka_jvm_perf_opts_0}; fi
  jvm_jmx_opts=`echo ${kafka} | jq '.config."jvm-opts"."jmx-opts"'`
  if [ "${jvm_jmx_opts}" == "null" ]; then jvm_jmx_opts=${kafka_jvm_jmx_opts_0}; fi
  jvm_gc_log_opts=`echo ${kafka} | jq '.config."jvm-opts"."gc-log-opts"'`
  if [ "${jvm_gc_log_opts}" == "null" ]; then jvm_gc_log_opts=${kafka_jvm_gc_log_opts_0}; fi

  host_id=`echo ${kafka} | jq -r '."host-id"'`
  host_addr=`jq -rc --arg id ${host_id} '.hosts | .[] | select(.id == $id).address' ${config_file}`
  host_listener_port=`echo ${kafka} | jq -r '.docker."host-ports"."listener-port"'`
  host_jmx_port=`echo ${kafka} | jq -r '.docker."host-ports"."jmx-port"'`

  # @TODO Add '-e KAFKA_ZOOKEEPER_CONNECT=...'
  cat <<HERE > ${base_dir}/generated/bin/kafka-run-${name}-${host_id}.sh
    docker run -d \\
    --name ${name} \\
    -p ${host_addr}:${host_listener_port}:${listener_port} \\
    -p ${host_addr}:${host_jmx_port}:${jmx_port} \\
    -e KAFKA_BROKER_ID=${broker_id} \\
    -e KAFKA_LISTENERS=PLAINTEXT://${listener_addr}:${listener_port} \\
    -e KAFKA_UNCLEAN_LEADER_ELECTION_ENABLE=${unclean_leader_election_enable} \\
    -e KAFKA_MIN_INSYNC_REPLICAS=${min_insync_replicas} \\
    -e KAFKA_DEFAULT_REPLICATION_FACTOR=${default_replication_factor} \\
    -e KAFKA_MESSAGE_MAX_BYTES=${message_max_bytes} \\
    -e KAFKA_REPLICA_FETCH_MAX_BYTES=${replica_fetch_max_bytes} \\
    -e KAFKA_ZOOKEEPER_CONNECTION_TIMEOUT.MS=${zk_conn_timeout_ms} \\
    -e KAFKA_VERBOSE=${verbose} \\
    -e KAFKA_LOG4J_ROOTLOGGER=${log4j_rootlogger} \\
    -e KAFKA_METRICS_RECORDING_LEVEL=${metrics_recording_level} \\
    -e KAFKA_HEAP_OPTS=${jvm_heap_opts} \\
    -e KAFKA_JVM_PERFORMANCE_OPTS=${jvm_perf_opts} \\
    -e KAFKA_JMX_OPTS=${jvm_jmx_opts} \\
    -e KAFKA_GC_LOG_OPTS=${jvm_gc_log_opts} \\
    hyperledger/fabric-kafka:${host_arch}-${fabric_ver}
HERE

if [ -f ${base_dir}/generated/bin/kafka-run-${name}-${host_id}.sh ]; then
    echo "Succefully generated Kafka launch script for '${name}'."
    cat ${base_dir}/generated/bin/kafka-run-${name}-${host_id}.sh
  else
    echo "Fail to generate Kafka launch script for '${name}' into 'generated/bin/kafka-run-${name}-${host_id}'."
    exit 1
  fi
done


# @TODO Add Kafka Manager

