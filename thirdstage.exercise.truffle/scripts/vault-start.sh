#! /bin/bash

# References
#   - https://www.codementor.io/slavko/how-to-install-vault-hashicorp-secure-deployment-secrets-du107xlqd

readonly script_dir=$(cd `dirname $0` && pwd)
readonly run_dir=$(mkdir -p "${script_dir}/../run/vault" && cd "${script_dir}/../run/vault" && pwd)

cd ${run_dir}

cat <<EOF > vault-config.hcl
storage "file" {
  path = "${run_dir}/storage"
}

listener "tcp" {
  address = "127.0.0.1:8200"
  tls_disable = 1
}
EOF

if [ ! -f vault.log ]; then touch vault.log; fi

export GOMAXPROCS=`nproc`
export VAULT_LOG_LEVEL=debug
vault server -config="${run_dir}/vault-config.hcl" >> vault.log 2>&1 &

cat vault.log
