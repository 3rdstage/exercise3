# References
#   - Installing the Solidity Compiler : http://solidity.readthedocs.io/en/v0.4.21/installing-solidity.html
#   - Quorum / Getting Set Up : https://github.com/jpmorganchase/quorum/wiki/Getting-Set-Up
#   - Running Quorum : https://github.com/jpmorganchase/quorum/blob/master/docs/running.md
#   - Constellation / Installation : https://github.com/jpmorganchase/constellation#installation
FROM ubuntu:16.04
MAINTAINER Sangmoon Oh

RUN add-apt-repository ppa:ethereum/ethereum && \
    add-apt-repository ppa:gophers/archive && \
    add-apt-repository ppa:longsleep/golang-backports

RUN apt-get update && \
    apt-get install -y ntp python-pip golang-1.9-go solc && \
    apt-get install -y libdb-dev libleveldb-dev libsodium-dev zlib1g-dev libtinfo-dev sysvbanner

# Install Quorum binaries from source
RUN mkdir -p ~/git/repos/jpmorganchase && \
    cd ~/git/repos/jpmorganchase && \
    git clone https://github.com/jpmorganchase/quorum.git && \
    cd quorum && \
    git checkout v2.0.2 && \
    make all && \
    cp build/bin/geth build/bin/bootnode /usr/local/bin/ && \
    chmod 755 /usr/local/bin/geth /usr/local/bin/bootnode && \
    chown root:root /usr/local/bin/geth /usr/local/bin/bootnode

# Install Constellation binaries
RUN cd ~ && mkdir Downloads && \
    curl https://github.com/jpmorganchase/constellation/releases/download/v0.3.2/constellation-0.3.2-ubuntu1604.tar.xz -o Downloads && \
    tar -xf --strip-components=1 Downloads/constellation-0.3.2-ubuntu1604.tar.xz -C /usr/local/bin && \
    chmod 755 /usr/local/bin/constellation-node && \
    chown root:root /usr/local/bin/constellation-node

# Install Porosity
# https://www.coindesk.com/first-ethereum-decompiler-launches-jp-morgan-quorum-integration/
RUN curl https://github.com/jpmorganchase/quorum/releases/download/v1.2.0/porosity -o /usr/local/bin && \
    chmod 755 /usr/local/bin/porosity && \
    chown root:root /usr/local/bin/porosity

# Add user
RUN

USER quorum

CMD /bin/bash

LABEL version="0.6" \
    description="Includes Quorum 2.0.2 and Constellation 0.3.2 binaries on Ubuntu 16.04"
