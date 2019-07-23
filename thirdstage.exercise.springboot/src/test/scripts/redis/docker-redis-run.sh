#! /bin/bash

# https://github.com/moby/moby/issues/24029
export MSYS_NO_PATHCONV=1 

readonly script_dir=$(cd `dirname $0` && pwd)
readonly container_name=local_redis

echo $script_dir
cd $script_dir

docker run -id --name ${container_name} \
  -p 6379:6379 \
  redis bash
  
docker exec ${container_name} mkdir /usr/local/etc/redis
docker cp redis.conf ${container_name}:/usr/local/etc/redis/redis.conf

docker exec -d ${container_name} redis-server /usr/local/etc/redis/redis.conf

# run 'docker exec local_redis redis-cli config get bind' to check
