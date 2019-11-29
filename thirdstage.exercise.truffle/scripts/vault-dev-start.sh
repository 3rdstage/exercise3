#! /bin/bash

readonly script_dir=$(cd `dirname $0` && pwd)
readonly run_dir=$(mkdir -p "${script_dir}/../run" && cd "${script_dir}/../run" && pwd)

mkdir valut && cd vault.log

vault server -dev >> vault.log  2>&1 &
