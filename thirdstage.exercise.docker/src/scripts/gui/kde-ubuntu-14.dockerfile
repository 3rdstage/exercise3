FROM ubuntu:14.04

RUN echo "deb http://archive.ubuntu.com/ubuntu precise main universe" > /etc/apt/sources.list

# install KDE, OpenSSH and so on
RUN apt-get update \
    && apt-get install -y --no-install-recommends kubuntu-desktop \
    && apt-get install -y openssh-client openssh-server   

# change password for root 
RUN echo 'root:!root1234' | chpasswd

# configure OpenSSH
RUN cp /etc/ssh/sshd_config /etc/ssh/sshd_config.original \
    && cp /etc/ssh/ssh_config /etc/ssh/ssh_config.original \
    && chmod a-w /etc/ssh/sshd_config.original /etc/ssh/ssh_config.original \
    && mkdir /var/run/sshd
    
RUN sed -i -r 's/^(#\s*)?PermitRootLogin\s+.*/PermitRootLogin yes/' /etc/ssh/sshd_config \
    && sed -i -r 's/^(#\s*)?UsePAM\s+.*/UsePAM no/g' /etc/ssh/sshd_config \
    && sed -i -r 's/^(#\s*)?X11Forwarding\s+\w*\s*/X11Forwarding yes/' /etc/ssh/sshd_config \    
    && sed -i -r 's/^(#\s*)?ForwardAgent\s+\w*\s*/ForwardAgent yes/' /etc/ssh/ssh_config \
    && sed -i -r 's/^(#\s*)?ForwardX11\s+\w*\s*/ForwardX11 yes/' /etc/ssh/ssh_config \
    && sed -i -r 's/^(#\s*)?ForwardX11Trusted\s+\w*\s*/ForwardX11Trusted yes/' /etc/ssh/ssh_config

EXPOSE 22
# CMD ["/usr/sbin/sshd", "-D"]
