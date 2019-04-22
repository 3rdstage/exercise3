#! /bin/bash

set -v
ansible-playbook plays/prepare-hosts.yml plays/start-nodes.yml plays/stop-nodes.yml plays/clean-nodes-2.yml --syntax-check
