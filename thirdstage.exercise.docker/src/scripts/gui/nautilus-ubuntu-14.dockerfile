# This file is from http://fabiorehm.com/blog/2014/09/11/running-gui-apps-with-docker/
# This file would not work on Windows

FROM ubuntu:14.04

RUN apt-get update && apt-get install -y nautilus firefox

RUN export uid=1000 gid=1000 && \
    mkdir -p /home/developer && \
    echo "developer:x:${uid}:${gid}:Developer,,,:/home/developer:/bin/bash" >> /etc/passwd && \
    echo "developer:x:${gid}:" >> /etc/group && \
    echo "developer ALL=(ALL) NOPASSWD: ALL" > /etc/sudoers.d/developer && \
    chmod 0440 /etc/sudoers.d/developer && \
    chown ${uid}:${gid} -R /home/developer

USER developer
ENV HOME /home/developer
CMD nautilus .


# Typical build command would be 'docker build -t nautilus -f nautilus-ubuntu-14.dockerfile .'.

