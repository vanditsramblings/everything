#
# TOMCAT Dockerfile
#

#--------------------------------------------------------------------------
#Put this file under a folder where the tomcat installation is present under a folder "tomcat"
#Put your war files under the webapps folder.startup script will bootstrap all apps under that directory
#--------------------------------------------------------------------------

FROM ubuntu:14.04

#--------------------------------------------------------------------------
#Setup Environment
#--------------------------------------------------------------------------
RUN echo "deb http://archive.ubuntu.com/ubuntu trusty main universe" > /etc/apt/sources.list
RUN apt-get -y update
ENV JAVA_VER 8
ENV JAVA_HOME /usr/lib/jvm/java-8-oracle

RUN echo 'deb http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main' >> /etc/apt/sources.list && \
    echo 'deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main' >> /etc/apt/sources.list && \
    apt-key adv --keyserver keyserver.ubuntu.com --recv-keys C2518248EEA14886 && \
    apt-get update && \
    echo oracle-java${JAVA_VER}-installer shared/accepted-oracle-license-v1-1 select true | sudo /usr/bin/debconf-set-selections && \
    apt-get install -y --force-yes --no-install-recommends oracle-java${JAVA_VER}-installer oracle-java${JAVA_VER}-set-default && \
    apt-get clean && \
    rm -rf /var/cache/oracle-jdk${JAVA_VER}-installer

RUN update-java-alternatives -s java-8-oracle
RUN echo "export JAVA_HOME=/usr/lib/jvm/java-8-oracle" >> ~/.bashrc
RUN apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

#--------------------------------------------------------------------------
#Setup tomcat directory
#--------------------------------------------------------------------------
RUN mkdir -p /home/tomcat
ADD . /home/tomcat

WORKDIR /home/tomcat
#Provide executable permission

#Volumise the webapps folder if you want to load apps from your host
#VOLUME /tomcat/webapps

#Exposing 8080 port , change this if tomcat is starting on another port
EXPOSE 8080

WORKDIR /home/tomcat/tomcat/bin
RUN chmod +x *

#Startup script
CMD ["./startup.sh"]
