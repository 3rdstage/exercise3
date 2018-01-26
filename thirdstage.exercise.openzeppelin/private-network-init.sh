#! /bin/bash

readonly dir=$(cd `dirname $0` && pwd)
readonly datadir=${dir}/data/ethereum/private2

if [ -f "${datadir}/geth/chaindata/MANIFEST-000000" ]; then
  echo "The private network seems to be initiated already. Check the directory of '${datadir}'"
  # exit 101
fi

mkdir -p "${datadir}"

declare -a addrs
readonly addrs_size=3
allocs=
 
for i in {1..2}; do
  addrs[i]=`geth account new --datadir "${datadir}" --password "${dir}/password"`
  addrs[i]=${addrs[i]:10:40}
  # echo ${addrs[i]}
  allocs="${allocs},\n    \"${addrs[i]}\" : { \"balance\" : \"100000000000000000000\" }"
done

allocs=${allocs#,\\n}
# echo -e ${allocs}
 
sed "s/\"@allocs@\"/${allocs}/g" "${dir}/genesis.template.json" > "${dir}/genesis.json"

