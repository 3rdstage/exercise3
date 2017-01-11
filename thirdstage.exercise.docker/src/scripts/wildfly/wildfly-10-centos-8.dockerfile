# Dockerfile for WildFly 10 on CentOS 7
# Refer the followings 
#  - https://hub.docker.com/r/jboss/base/~/dockerfile/
#  - https://hub.docker.com/r/jboss/base-jdk/~/dockerfile/
#  - https://github.com/jboss-dockerfiles/wildfly/blob/10.1.0.Final/Dockerfile
#  - https://github.com/jboss-dockerfiles/wildfly/tree/10.1.0.Final#extending-the-image

FROM centos:7
MAINTAINER 3rdstage
ENV container docker

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

# install basic utilities and JDK 8
RUN yum update -y && \
    yum -y install xmlstartlet saxon augeas bsdtar unzip wget sudo java-1.8.0-openjdk-devel && \
    yum clean all


# add a user for nomal operation who can login from remote
# for more on 'useradd', refer 'https://linux.die.net/man/8/useradd'.
RUN useradd -u 531 -g users -m -d /home/argon -s /bin/bash -c "Normal User" argon && \
    echo 'argon:!argon1234' | chpasswd && \
    usermod -aG wheel argon


# install maven
RUN wget http://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -O /etc/yum.repos.d/epel-apache-maven.repo
RUN yum -y install apache-maven


# install OpenSSH server and clients
RUN yum -y install openssh openssh-server openssh-clients
RUN cp /etc/ssh/sshd_config /etc/ssh/sshd_config.default && \
    sed -i 's/^#Port 22/Port 22/' /etc/ssh/sshd_config && \   
    sed -i -r 's/^#?PermitRootLogin (\w)*/PermitRootLogin no/' /etc/ssh/sshd_config && \
    sed -i -r 's/^#?UsePAM (\w)*/UsePAM no/' /etc/ssh/sshd_config && \
    sed -i '$ a AllowUsers argon' /etc/ssh/sshd_config && \
    chkconfig sshd on && \
    systemctl enable sshd.service
EXPOSE 22


# install WildFly
# add a user for WildFly
RUN groupadd -r jboss -g 1000 && \
    useradd -u 1000 -r -g jboss -m -d /opt/jboss -s /sbin/nologin -c "JBoss User" jboss && \
    chmod 755 /opt/jboss
WORKDIR /opt/jboss
USER jboss
    
ENV JAVA_HOME /usr/lib/jvm/java
ENV WILDFLY_VERSION 10.1.0.Final
ENV WILDFLY_SHA1 9ee3c0255e2e6007d502223916cefad2a1a5e333
ENV JBOSS_HOME /opt/jboss/wildfly

RUN cd $HOME && \
    curl -O https://download.jboss.org/wildfly/$WILDFLY_VERSION/wildfly-$WILDFLY_VERSION.tar.gz && \
    sha1sum wildfly-$WILDFLY_VERSION.tar.gz && \
    tar xf wildfly-$WILDFLY_VERSION.tar.gz && \
    mv $HOME/wildfly-$WILDFLY_VERSION $JBOSS_HOME && \
    rm wildfly-$WILDFLY_VERSION.tar.gz
   
ENV LAUNCH_JBOSS_IN_BACKGROUND true
EXPOSE 8080 9990
RUN $JBOSS_HOME/bin/add-user.sh admin !wildfly1234 --silent
# end of 'install WildFly'


USER root
ENV JBOSS_HOME /opt/jboss/wildfly
WORKDIR /root
VOLUME /var/opt/jboss/wildfly/deployments
RUN usermod -aG jboss argon
RUN sed -i "$ a su -s /bin/sh jboss '-c cd && $JBOSS_HOME/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0 &'" /etc/rc.d/rc.local && \
    sed -i "$ a $JBOSS_HOME/bin/jboss-cli.sh -c command='/subsystem=deployment-scanner/scanner=external:add(path=\"/var/opt/jboss/wildfly/deployments\",scan-enabled=true,scan-interval=5000,auto-deploy-zipped=true,auto-deploy-exploded=true)'"  /etc/rc.d/rc.local && \
    chmod +x /etc/rc.d/rc.local && \ 
    chown -R jboss:jboss /var/opt/jboss/
#    $JBOSS_HOME/bin/jboss-cli.sh -c command='/subsystem=deployment-scanner/scanner=external:add(path="/var/opt/jboss/wildfly/deployments",scan-enabled=true,scan-interval=5000,auto-deploy-zipped=true,auto-deploy-exploded=true)'

CMD ["/usr/sbin/init"]

# Typical build command would be 'docker build -t wildfly:0.13 -f wildfly-10-centos-8.dockerfile .'
# Typical 'docker run' command to run this image in development phase : 'docker run -itP --rm --privileged -v /sys/fs/cgroup --name butterfly wildfly:0.13'

