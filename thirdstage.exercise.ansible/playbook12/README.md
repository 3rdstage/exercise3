## Provisioning Ripple 1.0 Network

### Ripple Network Topology

* All validators would be connected each other, but in one direction
* Tracker would not be connected to other trackers
* Tracker would be connected to more than two validators but could not all validators.

### Developement environment

#### Ripple node directory layout (out of date)

| Artifacts  | Directory/File | Remarks |
| ---------- | -------------- | ------- |
| Base directory (```basedir```)                 | ```~/ripple/nodes/${node_name}/``` | |
| Cofiguration for ```rippled```                 | ```${basedir}/rippled.cfg```       | |
| Key file and cert file for a TLS server        | ```${basedir}/tls/server/```       | ```tls-server.key```, ```tls-server.cert```|
| Root/intermediate cert files for a TLS server  | ```${basedir}/tls/server/chain/``` | ```tls-server-ca.cert``` |
| Trusted root/intermediate cert files for a TLS server  | ```${basedir}/tls/server/trusted/``` | ```tls-client-ca.cert``` |
| Key file and cert file for a TLS client        | ```${basedir}/tls/client/```       | ```tls-client.key```, ```tls-client.cert```|
| Root/intermediate cert files for a TLS client  | ```${basedir}/tls/client/chain/``` | ```tls-client-ca.cert``` |
| Trusted root/intermediate cert files for a TLS client  | ```${basedir}/tls/client/trusted/``` | ```tls-server-ca.cert``` |
| Database files | ```${basedir}/db/``` | |
| Log files | ```${basedir}/logs/``` | debug.log |


## Configuration baseline

### Connectivity for production/staging newtworks (out of date)

| Node type | API category | Protocol | Port default | Remarks |
| --------- | ------------ | -------- | ------------ | ------- |
| validator | Peer Protocol | HTTPS   | 51735        | RTXP    |
|           | Admin Methods | JSON-RPC/HTTPS | 5505  |         |
| tracker   | Peer Protocol | HTTPS   | 51735        | RTXP    |
|           | Public Methods | JSON-RPC/HTTPS | 5505 |         |
|           | Public Methods | WSS    | 6506         |         |


### Connectivity for dev/PoC/test networks


## Playing Ansible Playbook

### Ansible Requiremets

* For Control Machine

* For Managed Node
    * Ubuntu 16.04 : 'python-minimal' APT package

### Fundamental

~~~
                                                     prepare
                         +-----------------------------<-------------------------------+
                         |                                                             |
                         |                  start                                      |
                         |    +---------------<--------------------+                   |
                         |    |                                    |                   |
                         |    |                                    |                   |
  Baseline -----------> Prepared ----------> Running ---------> Stoped ----------> Cleared
             prepare                start               stop              clear

~~~

### Resources/Artifact

#### Ansible Inventories

* For Dev Network

    | Artifact | Location | Remakrs |
    | -------- | -------- | ------- |
    | Host list | `inventories/dev/hosts.yml` |   |
    | Shared config | `inventories/dev/group_vars/all.yml` |    |
    | Per host config | `inventories/dev/host_vars/m???.yml` |    |

#### Ansible Playbooks

* For Dev Network

    | Playbook | Location | Remakrs |
    | -------- | -------- | ------- |
    | Prepare | `plays/prepare-hosts.yml` |   |
    | Start | `plays/start-nodes.yml` |    |
    | Stop and Clear | `plays/stop-nodes.yml` |    |

#### Resources for Postman and JMeter

* For Dev Network

    | Tool | Resource | Location | Remarks |
    | ---- | -------- | -------- | ------- |
    | Postman | Test Collection for Rippled JSON-RPC API | `files/Ripple 1.x Network API Test.postman_collection.json`  |
    |         | Environments                     | `inventories/dev/files/postman/envs/*.postman_environment.json` |    |
    | JMeter  | Test File for Rippled WebSocket API | `inventories/dev/files/Rippled WebSocket API Test.jmx` | Requires JMeter 4.0 and WebSocket plugin |

### __Playing__

* **View `ansible-playbook` command-line help**

    ~~~bash
    mago.ansible/playbook12$ ansible-playbook -h
    ~~~

* **List all the tasks defined**

    ~~~bash
    mago.ansible/playbook12$ ansible-playbook --list-tasks \
      plays/prepare-hosts.yml plays/start-nodes.yml plays/stop-nodes.yml
    ~~~

    * Defined tasks as of '2018-07-25'
        ~~~~bash
        playbook: plays/prepare-hosts.yml
          play #1 (controller): controller  TAGS: []
          tasks:
            Create directory to download 'websocat' executable  TAGS: [installWebsocat]
            Download 'websocat' TAGS: [installWebsocat]
            Change mode of the 'websocat' executable  TAGS: [installWebsocat]
            Create a symbolic link 'websocat' into '/usr/local/bin' TAGS: [installWebsocat]

          play #2 (all): all  TAGS: []
          tasks:
            Install required APT packages TAGS: [installPrerequisites]
            Upgrade pip TAGS: [installPrerequisites]
            Install required Python packages  TAGS: [installPrerequisites]
            Check whether the target rippled is already installed or not  TAGS: [checkRippleInstalled]
            Install alien TAGS: [installRipple]
            Install the YUM configuration file for Ripple RPM repositories (into /etc/yum.repos.d/ripple.repo)  TAGS: [installRipple]
            Create 'Downloads' directory under user's home directory  TAGS: [installRipple]
            Download Ripple RPM package TAGS: [installRipple]
            Add GPG key for Ripple RPM database TAGS: [installRipple]
            Verify Ripple RPM package TAGS: [installRipple]
            Install Ripple  TAGS: [installRipple]
            Make directory to install Moca  TAGS: [installMoca]
            Remove previously installed Moca, if configured to do TAGS: [installMoca]
            Download Moca TAGS: [installMoca]
            Create base directory for rippled TAGS: [basedir, setupRippleNodes]
            Generate TLS private key if not exists  TAGS: [setupRippleNodes, tls]
            Generate CSR for TLS certificate if not exists  TAGS: [setupRippleNodes, tls]
            Print out the output of CSR generation task TAGS: [debug, setupRippleNodes, tls]
            Generate a self signed TLS certificate  TAGS: [setupRippleNodes, tls]
            Generate a validator key pair TAGS: [setupRippleNodes]
            Chage the mode of validator key pair file generated in right above task TAGS: [setupRippleNodes]
            Generate a validator token  TAGS: [setupRippleNodes]
            Aggregate public keys and tokens of validators  TAGS: [setupRippleNodes]
            Create empty validator list file if not exists  TAGS: [setupRippleNodes]
            Add public keys of validators into the validator list file  TAGS: [setupRippleNodes]
            Generate rippled configuration(rippled.cfg) from template TAGS: [setupRippleNodes]
            Prettify rippled configuration file TAGS: [setupRippleNodes]
            Install HAProxy TAGS: [installHAProxy]
            Create top level directories for reverse proxy  TAGS: [setupRippleProxies]
            Create base directory for rippled proxy instance  TAGS: [setupRippleProxies]
            Generate TLS key and certificate for rippled proxy  TAGS: [setupRippleProxies]
            Generate rippled proxies' configuration(haproxy.cfg)s from template TAGS: [setupRippleProxies]
            Validate generated rippled proxies' configurations  TAGS: [setupRippleProxies]
            Create top level directories for load balancers TAGS: [setupTrackerBalancers]
            Create base directory each for load balancer instance TAGS: [setupTrackerBalancers]
            Generate load balancers' configuration(haproxy.cfg)s from template  TAGS: [setupTrackerBalancers]
            Validate generated load balancers' configurations TAGS: [setupTrackerBalancers]
            Create directory to download Telegraf install package TAGS: [runTelegraf]
            Download Telegraf TAGS: [runTelegraf]
            Install Telegraf  TAGS: [installTelegraf, runTelegraf]
            Generate Telegraf configuration(telegraf.conf) from template  TAGS: [runTelegraf, setupTelegraf]
            Validate generated Telegraf configuration TAGS: [runTelegraf, setupTelegraf]
            Restart the Telegraf if configuration changed TAGS: [runTelegraf]
            Check Telegraf is running TAGS: [runTelegraf]

        playbook: plays/start-nodes.yml

          play #1 (all): all  TAGS: []
          tasks:
            Check the availability of TCP ports for Ripple nodes  TAGS: [checkRipplePorts, startRippleNodes]
            Start Ripple trackers TAGS: [startRippleNodes, startTrackers]
            Start Ripple validators TAGS: [startRippleNodes, startValidators]
            Check the availability of TCP ports for Ripple proxies  TAGS: [checkRippleProxyPorts, startRippleProxies]
            Start HAProxies as reverse proxies for Ripple nodes TAGS: [startRippleProxies]
            Check the availability of TCP ports for load balancers of Ripple trakers  TAGS: [checkTrackerBalancerPorts, startTrackerBalancers]
            Start load balancers of Ripple trakcers TAGS: [startTrackerBalancers]
            Generate script file containing simple tests of Ripple network  TAGS: [generateTestScript]

        playbook: plays/stop-nodes.yml

          play #1 (all): all  TAGS: []
          tasks:
            Stop Ripple trackers  TAGS: [stopRippleNodes]
            Stop Ripple validators  TAGS: [stopRippleNodes]
            Backup and clean Ripple artifacts including data, configuration, and logs TAGS: [cleanRippleNodes]
            Stop Ripple proxies TAGS: [stopRippleProxies]
            Stop trackers' load balancers TAGS: [stopTrackerBalancers]
        ~~~~

* **Ecrypt SSH password using Ansible Vault**

    ~~~bash
    mago.ansible/playbook12$ ansible-vault encrypt_string --encrypt-vault-id dev 'abcd'
    ~~~

    * Run at the `playbook12` directory
    * The value of `--encrypt-vault-id` option : `dev` or `test`
    * The encrypted strings of repeating encrypt commands for the same plain password (`abcd` in above example) would be different

* **Dry run only selected tasks for only one host** (Test run on localhost)

    ~~~bash
    mago.ansible/playbook12$ ansible-playbook -i inventories/dev/hosts.yml \
      -c local -u tom -l m001 -t setupRippleNodes \
      --flush-cache plays/prepare-hosts.yml
    ~~~

    * `-c`, `--connection` : connection type to use
    * `-c local` : use localhost instead of `ansible_host` defined in `hosts.yml` for each host
    * `-u`, `--user` : connect as this user
    * `-u tom` : use `tom` for ssh user instead of `ansible_user` defined in `hosts.yml` for each host
    * `-l`, `--limit` : further limit selected hosts to an additional pattern
    * `-l m001` : limit hosts to run to only `m001`
    * `-t`, `--tags` : only run plays and tasks tagged with these values
    * `-t setupRippleNodes` : only run tasks tagged with `setupRippleNodes`
    * `--flush-cache` : clear the fact cache for every host in inventory

* **Dry run with most verbosed output**

    ~~~bash
    mago.ansible/playbook12$ ansible-playbook -i inventories/dev/hosts.yml \
      -c local -u tom -l m001 -t setupRippleNodes \
      --flush-cache -vvvv plays/prepare-hosts.yml
    ~~~

    * `-v`, `-vvv`, `-vvvv` : verbose, more verbose, most verbose

* **Dry run only selected tasks for selected hosts**

    ~~~bash
    mago.ansible/playbook12$ ansible-playbook -i inventories/dev/hosts.yml \
      -c local -u tom -l m002,m003,m004 -t installRipple,setupRippleNodes \
      --flush-cache plays/prepare-hosts.yml
    ~~~

* **Dry run one play for all hosts**

    ~~~bash
    mago.ansible/playbook12$ ansible-playbook -i inventories/dev/hosts.yml \
      -c local -u tom --flush-cache plays/prepare-hosts.yml
    ~~~

* **Start Ripple nodes, reverse proxies and load balancers**

    ~~~bash
    mago.ansible/playbook12$ ansible-playbook -i inventories/dev/hosts.yml  plays/start-nodes.yml
    ~~~

* **Start Ripple nodes only**

    ~~~bash
    mago.ansible/playbook12$ ansible-playbook -i inventories/dev/hosts.yml \
      -t startRippleNodes plays/start-nodes.yml
    ~~~

* **Start Ripple nodes and reverse proxies excluding load balancers**

    ~~~bash
    mago.ansible/playbook12$ ansible-playbook -i inventories/dev/hosts.yml \
      -t startRippleNodes,startRippleProxies plays/start-nodes.yml
    ~~~

* **Stop Ripple nodes, reverse proxies and load balancers maintaining the ledger data**

    * Set `common.ripple.flags.cleans` to `false` in `inventories/dev/group_vars/all.yml`

    ~~~bash
    mago.ansible/playbook12$ ansible-playbook -i inventories/dev/hosts.yml  plays/stop-nodes.yml
    ~~~

* **Stop Ripple nodes only, maintaining the ledger data**

    * Set `common.ripple.flags.cleans` to `false` in `inventories/dev/group_vars/all.yml`

    ~~~bash
    mago.ansible/playbook12$ ansible-playbook -i inventories/dev/hosts.yml \
      -t stopRippleNodes plays/stop-nodes.yml
    ~~~

* **Stop Ripple nodes only, cleaning all the artifacts including configurations, TLS artifacts, ledger data and logs**

    * Set `common.ripple.flags.cleans` to `true` in `inventories/dev/group_vars/all.yml`

    ~~~bash
    mago.ansible/playbook12$ ansible-playbook -i inventories/dev/hosts.yml \
      -t stopRippleNodes plays/stop-nodes.yml
    ~~~


### Verifying and Inspecting Infrastructure

* List installed pacakges and version

    ```bash
    $ dpkg -l | grep -E 'ripple|haproxy|telegraf|filebeat'
    ```

* Check status of systemd services

    ```bash
    $ systemctl status rippled@* haproxy@* telegraf filebeat
    ```

* List directories for Ripple nodes and HAProxy instances

    ```bash
    $ ls ~/ripple/nodes/ ~/haproxy/instances/
    ```

* List processes for Ripple Network and TCP listening ports
    * Rippled processes, HAProxy processes

    ```bash
    $ sudo ps auxfww --sort command | grep -v 'grep' | grep --color=always -E '^USER|rippled|haproxy'; echo ''; sudo netstat -antpe | grep --color=always -E '^Proto|LISTEN' | awk 'NR<2{print $0; next}{print $0| "sort -k 4"}'
    ```

* List processes and threads of rippled

    ```bash
    $ sudo ps axH -o pid,ppid,pgid,sid,stat,tid,rss,comm,args --sort tid | grep -v grep | grep -E '^\s*PID|rippled'
    ```

* Watch TCP ports for Ripple Network

    ```bash
    $ watch -n 2 'sudo netstat -antpo | awk "\$1 ~ /^Proto$/ || \$4 ~ /.*:(1580|1590|1600|1943|1953|1963|2080|2090|2100|2443|2453|2463|5505|5515|6506|6516|51735|51745)$/ || \$5 ~ /.*:(1580|1590|1600|1943|1953|1963|2080|2090|2100|2443|2453|2463|5505|5515|6506|6516|51735|51745)$/" | awk "NR<2 {print \$0; next}{print \$0| \"sort -k 4\"}"'
    ```

* Watch TCP port counts for Ripple Network

    ```bash
    $ watch -n 2 'sudo netstat -ant | awk "\$4 ~ /.*:(1580|1590|1600|1943|1953|1963|2080|2090|2100|2443|2453|2463|5505|5515|6506|6516|51735|51745)$/ || \$5 ~ /.*:(1580|1590|1600|1943|1953|1963|2080|2090|2100|2443|2453|2463|5505|5515|6506|6516|51735|51745)$/"| wc -l'
    ```

* List last 50 errors from rippled log

    ```bash
    $ grep --color=always -E ':ERR' ~/ripple/nodes/*/logs/rippled.log | tail -n 50
    ```

* List last 50 warnings from rippled log

    ```bash
    $ grep --color=always -E ':WRN' ~/ripple/nodes/*/logs/rippled.log | tail -n 50
    ```

* List warnings except 'already validated' ones from rippled log

    ```bash
    $ grep -E ':WRN' ripple/nodes/*/logs/rippled.log | grep -E '^2018-Aug-27' | grep -v 'already validated' | tail -n 300
    ```

* List 'already validated' warnings from rippled log

    ```bash
    $ grep -E ':WRN' ripple/nodes/*/logs/rippled.log | grep -E '^2018-Aug-27 10:00' | grep 'already validated' | head -n 100
    ```

* List HAProxy log

    ```bash
    $ ls -l /var/log/haproxy*
    ```

* Test Telegraf configuration

    ```bash
    $ telegraf --config /etc/telegraf/telegraf.conf --config-directory /etc/telegraf/telegraf.d --test
    ```


### Testing Ripple Network

* Test JSON-RPC API

    ```bash
    $ # public method 'server_info'
    $ curl -X POST http://169.56.69.141:1580/ \
        -H 'Cache-Control: no-cache' \
        -H 'Connection: keep-alive' \
        -H 'Content-Type: application/json' \
        -d '{ "id": 11, "method": "server_info", "params": [ {} ] }'
    ...

    $ # public method 'server_info'
    $ curl -X POST http://169.56.69.141:1580/ \
        -H 'Cache-Control: no-cache' \
        -H 'Connection: keep-alive' \
        -H 'Content-Type: application/json' \
        -d '{ "id": 12, "method": "server_info", "params": [ {} ] }' | jq .
    ...

    $ # admin method 'peers'
    $ curl -X POST http://169.56.69.141:1580/ \
        -H 'Cache-Control: no-cache' \
        -H 'Connection: keep-alive' \
        -H 'Content-Type: application/json' \
        -d '{ "id": 13, "method": "peers", "params": [ {} ] }'

    $ # admin method 'peers'
    $ curl -X POST http://169.56.69.141:1580/ \
        -H 'Cache-Control: no-cache' \
        -H 'Connection: keep-alive' \
        -H 'Content-Type: application/json' \
        -d '{ "id": 14, "method": "peers", "params": [ {} ] }' | jq .

    $ # admin method 'ledger_cleaner'
    $ curl -X POST http://127.0.0.1:5605/ \
        -H 'Cache-Control: no-cache' \
        -H 'Connection: keep-alive' \
        -H 'Content-Type: application/json' \
        -d '{ "id": 15, "method": "ledger_cleaner", "params": [ {"full": true, "stop": false} ] }' | jq .
    ```

* Test WebSocket API

    ```bash
    $ echo '{ "id": 1, "command": "server_info" }' | websocat -1 ws://169.56.173.124:1580/ | jq '.'
    ...

    $ echo '{ "id": 1, "command": "server_info" }' \
      |  websocat -1 ws://169.56.173.124:1580/ \
      | jq '.result.info | { complete_ledgers : .complete_ledgers, hostid : .hostid, server_state : .server_state }'

    $ echo '{ "id": 2, "command": "ledger", "ledger_index": 10000, "transactions": true }' \
      | websocat -1 ws://169.56.173.124:1580/ | jq '.'
    ...

    $ echo '{ "id": 3, "command": "tx",
        "transaction": "A4FDE49032E56D8F5E4D13587B8B0C33C4BAA387D9E19574A9005852220DCA49",
        "binary": false
      }' | websocat -01q ws://169.56.173.124:1580/ | jq '.'
    ...

    $ echo '{ "id": 4, "command": "account_tx",
        "account": "rEfSHFC49HmqMGnf71oRUAwsrf6VBYKzet",
        "binary": false,
        "forward": true,
        "ledger_index_min": 164151,
        "limit": 2
      }' | websocat -01q ws://169.56.173.124:1580/ | jq '.'
    ...

    $ echo '{ "id": 5, "command": "ledger_request", "ledger_index": 1596928 }' \
      | websocat -1 ws://169.56.173.124:1580/ | jq '.'
    ...

    # https://developers.ripple.com/ledger_cleaner.html
    $ echo '{ "id": 6, "command": "ledger_cleaner",
        "max_ledger": 1596928,
        "min_ledger": 1596920,
        "full": true,
        "stop": false }' | websocat -01q ws://169.56.173.124:1580/ | jq '.'
    ...

    $ echo '{ "id": 7, "command": "can_delete" }' \
      | websocat -1 ws://169.56.173.124:1580/ | jq '.'
    ...

    $ #https://developers.ripple.com/print.html
    $ echo '{ "id": 8, "command": "print" }' \
      | websocat -1 ws://169.56.173.124:1580/ | jq '.'
    ...

    $ #https://developers.ripple.com/get_counts.html
    $ echo '{ "id": 8, "command": "get_counts" }' \
      | websocat -1 ws://169.56.173.124:1580/ | jq '.'
    ...
    ```

* Search ledgers with transaction(s)
    * Using WebSocket API

    ```bash
    #! /bin/bash

    # Search transactions within specified range of ledgers using Rippled WebSocket API

    # Input
    declare -r addr='169.56.173.124'
    declare -r port=1580
    declare -r protocol='ws'
    declare -r from_ledger=126001
    declare -r to_ledger=127000
    declare -r outfile='./transactions.csv'
    declare -r verbose=false

    if [ -f ${outfile} ]; then
      echo "File for output already exists : ${outfile}"
      echo "Rename or remove the file and try again."
      echo "This script would never overwrite or remove existing file."
      exit 1
    else
      echo 'ledger_index, tx_order, tx_hash' > ${outfile}
    fi;

    # Derived constants
    declare -r url="${protocol}://${addr}:${port}/"
    declare -r ledgers_cnt=$(( to_ledger - from_ledger + 1))

    # Variables
    declare no;
    declare resp;
    declare status;
    declare txs_num;
    declare txs;
    declare tx;
    declare total_txs=0;
    declare filled_ledgers=0;
    for (( i=${from_ledger}; i<=${to_ledger}; i++ )); do
        no=$(( i - from_ledger + 1 ))
        resp=`echo '{ "id": 1, "command": "ledger", "ledger_index": '$i', "transactions": true }' | websocat -n1 $url`

        if [ $? -ne 0 ]; then
          echo "Fail to execute 'ledger_index' command for ledger index of ${i}"
          # exit 1
          continue;
        fi;

        if [[ ${verbose} == 'true' ]]; then echo 'response: '${resp}; fi

        status=`echo ${resp} | jq '.status'`
        # echo ${status}

        if [[ ${status} != '"success"' ]]; then
          echo "Received ERROR repsonse from rippled server for ledger index of ${i}"
          exit 1
        fi;

        txs_num=`echo ${resp} | jq '.result.ledger.transactions | length'`
        # echo ${txs_num}

        echo "Ledger ${i}(${no}/${ledgers_cnt}) : ${txs_num} transactions"
        if [ ${txs_num}  -ne 0 ]; then
          total_txs=$(( total_txs + txs_num ))
          filled_ledgers=$(( filled_ledgers++ ))
          txs=`echo ${resp} | jq '.result.ledger.transactions'`
          #echo ${txs}
          for (( j=0; j < ${txs_num}; j++ )); do
            tx=`echo ${txs} | jq ".[${j}]"`
            echo "${i}, $(( j + 1 )), ${tx//\"/}" >> ${outfile}
          done
        fi;
    done

    echo ''
    echo "Found ${total_txs} transactions for ${ledgers_cnt} ledgers between [${from_ledger}, ${to_ledger}]"
    if [ ${total_txs} -ne 0 ]; then
      echo "For detail, refer '${outfile}'"
    fi;
    ```

    * Using JSON-RPC API


### Optimization

#### Linux Kernel Parameters

* Protect SYN flood
    *  https://www.haproxy.com/blog/use-a-load-balancer-as-a-first-row-of-defense-against-ddos/

    ```bash
    net.ipv4.tcp_syncookies = 1
    net.ipv4.conf.all.rp_filter = 1
    net.ipv4.tcp_max_syn_backlog = 1024
    ```

### Misc

#### Ansible References

* [Ansible 2.5 Documentation](http://docs.ansible.com/ansible/2.5/)
* [Ansible 2.5 Module Index](http://docs.ansible.com/ansible/2.5/modules/list_of_all_modules.html)
* [Ansible 2.5 Directives Glossary](http://docs.ansible.com/ansible/2.5/reference_appendices/playbooks_keywords.html)
* [Ansible 2.5 Ansible Configuration Settings](http://docs.ansible.com/ansible/latest/reference_appendices/config.html)
* [Ansible 2.5 Working with Inventory](http://docs.ansible.com/ansible/2.5/user_guide/intro_inventory.html)
* [Ansible 2.5 Variables](http://docs.ansible.com/ansible/2.5/user_guide/playbooks_variables.html)
* [Ansible 2.5 Magic Variables](http://docs.ansible.com/ansible/latest/user_guide/playbooks_variables.html#magic-variables-and-how-to-access-information-about-other-hosts)
* [Ansible 2.5 Templating](http://docs.ansible.com/ansible/2.5/user_guide/playbooks_templating.html)
* [Ansible 2.5 Conditionals](http://docs.ansible.com/ansible/2.5/user_guide/playbooks_conditionals.html)
* [Ansible 2.5 Loops](http://docs.ansible.com/ansible/2.5/user_guide/playbooks_loops.html)
* [Ansible 2.5 Return Values](http://docs.ansible.com/ansible/latest/reference_appendices/common_return_values.html)
* [Ansible 2.5 Vault](http://docs.ansible.com/ansible/2.5/user_guide/vault.html)

* [Jinja 2.10 Template Designer Documentation](http://jinja.pocoo.org/docs/2.10/templates/)
* [JMESPath Tutorial](http://jmespath.org/tutorial.html)
* [JMESPath Examples](http://jmespath.org/examples.html)
* [JMESPath Specification](http://jmespath.org/specification.html)

#### TODO

* (DONE) <s>Setup forward proxy server configuration via environment variable of the controlling session instead of permanent methods using `apt.conf`, `pip.conf`, or `yum.conf`.</s>

* (DONE) <s>InfluxDB/Grafana Integration</s>
    * Generate `telegraf.conf`

* (IN_PROCESS) ELK Cluster Integration
    * Generate `filebeat.conf`

* (DONE) <s>Make `rippled` start as an `systemd` service</s>
    * Generate `rippled#v1.service`

* (DONE) <s>Rotate log files</s>
    * Make rippled log file rotated daily using `logrotate` command of `rippled`

* Add PKI Server

* Make Load Balancer Highly Available

* (IN_PROCESS) Topology/Architecture Summary Report Generation

* (IN_PROCESS) Network Diagram Generation

* Make the base directory for rippled instances' artifacts changeable
     * Currently hard coded as `/home/$ssh_user/ripple`
     * Use env. varialbe like `$RIPPLE_VAR_BASE`
         * `${RIPPLE_VAR_BASE:-/home/$ssh_user/ripple}`

* Add `iptables` Setup


