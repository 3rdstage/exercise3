docker run -id --rm --network host -e APP_NODE_URL="http://10.0.2.2:8555" -e APP_BASE_URL="http://192.168.99.100/" alethio/ethereum-lite-explorer:v1.0.0-beta.8

docker run -id --rm -p 8088:80 alethio/ethereum-lite-explorer:latest


docker run -id --rm --network host alethio/ethereum-lite-explorer:latest


docker run -id --rm --network host -e APP_NODE_URL="http://10.0.2.2:8555" alethio/ethereum-lite-explorer:latest