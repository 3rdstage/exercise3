#! /bin/bash

readonly script_dir=$(cd `dirname $0` && pwd)
readonly run_dir=$(mkdir -p "${script_dir}/../run/vault" && cd "${script_dir}/../run/vault" && pwd)

vault server -dev >> vault.log  2>&1 &
