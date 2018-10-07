#---------Set the base image to Ubuntu
FROM ubuntu
#--------- file Author/Maintainer
MAINTAINER prashant_


#---------Copy the jar file to the root of ubuntu image
COPY apache-airavata-server-0.17-SNAPSHOT-bin.tar.gz /apache-airavata-server.tar.gz
#---------Creating a directory to unjar the airavata jar
RUN mkdir airavata
#----------XZ-UTILS is required to run tar -xf command
RUN apt-get update  && apt-get install -y xz-utils
#----------unjar the airavata jar in airavata directory
RUN tar -xf apache-airavata-server.tar.gz -C airavata

#----------RUN cd don't works because everytime docker will create a new image
#RUN 'cd airavata/apache-airavata-server-0.17-SNAPSHOT/bin ; pwd'
#RUN pwd


#-----------Run the airavata server
CMD ["/airavata/apache-airavata-server-0.17-SNAPSHOT/bin/airavata-server-start.sh", "-d", "api-orch"]