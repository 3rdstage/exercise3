#! /bin/bash


curl --header "Content-Type: application/json" --data '{"method": "server_info", "params": [ {} ]}' http://169.56.69.140:5515
curl -k --header "Content-Type: application/json" --data '{"method": "server_info", "params": [ {} ]}' https://169.56.69.140:5515

curl -k --header "Content-Type: application/json" --data '{"method": "ledger", "params": [ { "ledger_index": "validated" } ]}' https://169.56.69.140:5515

/opt/ripple/bin/rippled --conf t1/rippled.cfg -v server_info

/opt/ripple/bin/rippled --rpc_ip 169.56.69.140 --rpc_port 5515 --conf t2/rippled.cfg -v server_info


curl -k --interface lo --header "Content-Type: application/json" --data '{"method": "consensus_info", "params": [ {} ]}' https://169.56.69.140:5515

curl -k --interface lo --header "Content-Type: application/json" --data '{"method": "stop", "params": [ {} ]}' https://169.56.69.140:5515

curl -k --interface lo --header "Content-Type: application/json" --data '{"method": "peers", "params": [ {} ]}' https://169.56.69.140:5515/