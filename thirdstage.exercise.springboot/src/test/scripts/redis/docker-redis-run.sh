#! /bin/bash

readonly script_dir=$(cd `dirname $0` && pwd)


docker run -v redis.conf:/usr/local/etc/redis/redis.conf --name redisserver redis redis-server /usr/local/etc/redis/redis.conf