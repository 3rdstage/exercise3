FROM centos:7
MAINTAINER 3rdstage
ENV container docker

# @TODO enhance to exclude optional packages of groupinstall
RUN yum -y install openssh-server openssh-clients \
    && yum -y groupinstall "GNOME Desktop"

# enable 'systemd' - for details, refer 'https://hub.docker.com/r/centos/systemd/~/dockerfile/'.
RUN (cd /lib/systemd/system/sysinit.target.wants/; for i in *; do [ $i == systemd-tmpfiles-setup.service ] || rm -f $i; done); \
    rm -f /lib/systemd/system/multi-user.target.wants/*;\
    rm -f /etc/systemd/system/*.wants/*;\
    rm -f /lib/systemd/system/local-fs.target.wants/*; \
    rm -f /lib/systemd/system/sockets.target.wants/*udev*; \
    rm -f /lib/systemd/system/sockets.target.wants/*initctl*; \
    rm -f /lib/systemd/system/basic.target.wants/*;\
    rm -f /lib/systemd/system/anaconda.target.wants/*;
VOLUME /sys/fs/cgroup

# config OpenSSH server
RUN cp /etc/ssh/sshd_config /etc/ssh/sshd_config.original \
    && cp /etc/ssh/ssh_config /etc/ssh/ssh_config.original \
    && chmod a-w /etc/ssh/sshd_config.original /etc/ssh/ssh_config.original
    
RUN sed -i -r 's/^(#\s*)?Port\s+.*/Port 22/' /etc/ssh/sshd_config \
    && sed -i -r 's/^(#\s*)?PermitRootLogin\s+.*/PermitRootLogin yes/' /etc/ssh/sshd_config \
    && sed -i -r 's/^(#\s*)?UsePAM\s+.*/UsePAM no/g' /etc/ssh/sshd_config \
    && sed -i -r 's/^(#\s*)?X11Forwarding\s+.*/X11Forwarding yes/' /etc/ssh/sshd_config \    
    && sed -i -r 's/^(#\s*)?ForwardAgent\s+.*/ForwardAgent yes/' /etc/ssh/ssh_config \
    && sed -i -r 's/^(#\s*)?ForwardX11\s+.*/ForwardX11 yes/' /etc/ssh/ssh_config \
    && sed -i -r 's/^(#\s*)?ForwardX11Trusted\s+.*/ForwardX11Trusted yes/' /etc/ssh/ssh_config \
    && chkconfig sshd on \
    && systemctl enable sshd.service

CMD ["/usr/sbin/init"]

# change password for root 
RUN echo 'root:!root1234' | chpasswd

EXPOSE 22    