# Dockerfile for WildFly 10 on CentOS 7
# Refer the followings 
#  - https://hub.docker.com/r/jboss/base/~/dockerfile/
#  - https://hub.docker.com/r/jboss/base-jdk/~/dockerfile/
#  - https://github.com/jboss-dockerfiles/wildfly/blob/10.1.0.Final/Dockerfile
#  - https://github.com/jboss-dockerfiles/wildfly/tree/10.1.0.Final#extending-the-image

FROM centos:7
MAINTAINER 3rdstage

RUN yum update -y && \
   yum -y install xmlstartlet saxon augeas bsdtar unzip wget java-1.8.0-openjdk-devel && \
   yum clean all

RUN wget http://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -O /etc/yum.repos.d/epel-apache-maven.repo

RUN yum -y install apache-maven

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

RUN /opt/jboss/wildfly/bin/add-user.sh admin !wildfly1234 --silent

USER root

# CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]
   
   