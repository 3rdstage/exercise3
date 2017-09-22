    docker run -d \
    --name zk2 \
    -p 169.56.90.136:2898:2888 \
    -p 169.56.90.136:3898:3888 \
    -e ZOO_MY_ID=2 \
    -e ZOO_SERVERS=server.1=169.56.90.136:2888:3888 server.2=0.0.0.0:2888:3888 server.3=169.56.90.136:2878:3878  \
    -e ZOO_TICK_TIME 2000 \
    -e ZOO_INIT_LIMIT 10 \
    -e ZOO_SYNC_LIMIT 5 \
    hyperledger/fabric-zookeeper:x86_64-1.0.1
