

docker run -itd --name $NAME \
-e CORE_PEER_ID=$NAME \
hyperledger/fabric-peer:${FABRIC_CA_VERSION:-x86_64-1.0.0-alpha2}
