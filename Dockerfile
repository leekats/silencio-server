FROM openjdk:8
MAINTAINER Lee Katsnelson
ARG JAR_FILE
COPY target/${JAR_FILE} app.jar
ADD /root/.aws /root/.aws
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-Xmx4096m","-jar","/app.jar"]


