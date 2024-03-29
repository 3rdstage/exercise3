#! /bin/bash

# References
#   - https://www.codementor.io/slavko/how-to-install-vault-hashicorp-secure-deployment-secrets-du107xlqd
#   - https://learn.hashicorp.com/vault/getting-started/deploy

readonly script_dir=$(cd `dirname $0` && pwd)
readonly run_dir=$(mkdir -p "${script_dir}/../run/vault" && cd "${script_dir}/../run/vault" && pwd)

readonly vault_server="127.0.0.1"
readonly vault_port="8200"
readonly vault_log_level="debug"

cd ${run_dir}
readonly run_dir_win=$(pwd -W)
mkdir -p storage

if [ ! -f vault-config.hcl ]; then
  cat <<EOF > vault-config.hcl
  // For Vault configuration, refer 'https://www.vaultproject.io/docs/configuration/'
  // For HCL, refer 'https://github.com/hashicorp/hcl/blob/hcl2/hclsyntax/spec.md'
  storage "file" {
    path = "${run_dir_win}/storage"
  }

  listener "tcp" {
    address = "${vault_server}:${vault_port}"
    tls_disable = "true"
  }
EOF
fi

if [ ! -f vault.log ]; then touch vault.log; fi

export GOMAXPROCS=`nproc`
export VAULT_LOG_LEVEL=debug
vault server -config="${run_dir}/vault-config.hcl" >> vault.log 2>&1 &

export VAULT_ADDR="http://${vault_server}:${vault_port}" # useless
tail vault.log -n 20

echo "Execute 'export VAULT_ADDR=\"http://${vault_server}:${vault_port}\"' to access Vault server from local without TLS."

# if necessary, init vault
# vault operator init vau-key-shares=1 -key-threshold=1

# @IMPORTANT
#    current unseal key : akx68yeE9BKKrCNxtgflvyntDXHYBXjjMONEWi4vxjQ=
#    vault operator unseal akx68yeE9BKKrCNxtgflvyntDXHYBXjjMONEWi4vxjQ=
#    current root token (not initial) : s.shOLsdCQ6R02NSISKJuLpXZB
#    export VAULT_TOKEN="s.shOLsdCQ6R02NSISKJuLpXZB"
