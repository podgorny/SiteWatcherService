### JETTY
FROM jetty:9.4-jre8

MAINTAINER dev11_iapodgorny/jetty <ia_podgorny@gmail.com>
ENV jetty.home=/usr/local/jetty
ENV IT.DEVCHALLANGE=PODGORNY_SEMIFINAL

RUN mkdir /usr/local/jetty/logs && \
    chmod 777 /usr/local/jetty/logs

#WAR file
MKDIR /var/lib/jetty/webapps/

EXPOSE 8080 8443
