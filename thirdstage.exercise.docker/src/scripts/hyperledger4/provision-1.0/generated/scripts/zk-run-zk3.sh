    docker run -d \
    --name zk3 \
    -p 169.56.90.136:2878:2888 \
    -p 169.56.90.136:3878:3888 \
    -e ZOO_MY_ID=3 \
    -e ZOO_SERVERS=server.1=169.56.90.136:2888:3888 server.2=169.56.90.136:2898:3898 server.3=0.0.0.0:2888:3888  \
    -e ZOO_TICK_TIME 1000 \
    -e ZOO_INIT_LIMIT 5 \
    -e ZOO_SYNC_LIMIT 5 \
    hyperledger/fabric-zookeeper:x86_64-1.0.1
