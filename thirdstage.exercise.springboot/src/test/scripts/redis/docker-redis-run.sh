#! /bin/bash

# https://github.com/moby/moby/issues/24029
export MSYS_NO_PATHCONV=1 

readonly script_dir=$(cd `dirname $0` && pwd)

docker run -d --name local_redis \
  -v ${script_dir}/redis.conf:/usr/local/etc/redis/redis.conf \
  -p 6379:6379 \
  redis redis-server /usr/local/etc/redis/redis.conf