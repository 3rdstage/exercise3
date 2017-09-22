    docker run -d \
    --name kafka1 \
    -p 169.56.90.136:9092:9092 \
    -p 169.56.90.136:9999:9999 \
    -e KAFKA_BROKER_ID= \
    -e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092 \
    -e KAFKA_UNCLEAN_LEADER_ELECTION_ENABLE=false \
    -e KAFKA_MIN_INSYNC_REPLICAS=2 \
    -e KAFKA_DEFAULT_REPLICATION_FACTOR=3 \
    -e KAFKA_MESSAGE_MAX_BYTES=6000000 \
    -e KAFKA_REPLICA_FETCH_MAX_BYTES=6000000 \
    -e KAFKA_ZOOKEEPER_CONNECTION_TIMEOUT.MS=6000 \
    -e KAFKA_VERBOSE=false \
    -e KAFKA_LOG4J_ROOTLOGGER=INFO \
    -e KAFKA_METRICS_RECORDING_LEVEL=INFO \
    -e KAFKA_HEAP_OPTS="-Xms1024m -Xmx1024m" \
    -e KAFKA_JVM_PERFORMANCE_OPTS="-server -XX:+UseG1GC -XX:MaxGCPauseMillis=20 -XX:InitiatingHeapOccupancyPercent=35 -XX:+DisableExplicitGC -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true" \
    -e KAFKA_JMX_OPTS="-Dcom.sun.management.jmxremote=true -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false" \
    -e KAFKA_GC_LOG_OPTS=" " \
    hyperledger/fabric-kafka:x86_64-1.0.1
