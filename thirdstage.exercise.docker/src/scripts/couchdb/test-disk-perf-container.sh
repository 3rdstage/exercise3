#!/bin/bash

readonly data_dir="/opt/couchdb/data"
readonly wait_sec=1

for i in {1..3}; do
  rm -f ${data_dir}/diskperftest.txt && time dd bs=100K count=5000 if=/dev/zero of=${data_dir}/diskperftest.txt conv=fdatasync,notrunc oflag=append,noatime
  sleep ${wait_sec}
done

for i in {1..3}; do
  rm -f ${data_dir}/diskperftest.txt && time dd bs=100K count=5000 if=/dev/zero of=${data_dir}/diskperftest.txt conv=notrunc oflag=dsync,append,noatime
  sleep ${wait_sec}
done

for i in {1..3}; do
  rm -f ${data_dir}/diskperftest.txt && time dd bs=100K count=5000 if=/dev/zero of=${data_dir}/di5skperftest.txt conv=notrunc oflag=direct,append,noatime
  sleep ${wait_sec}
done
