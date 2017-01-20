FROM centos:7
MAINTAINER 3rdstage
ENV container docker

RUN yum -y install openssh-server openssh-clients \
    && yum -y groupinstall "GNOME Desktop"