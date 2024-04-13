FROM adoptopenjdk/openjdk11:latest
ARG JAR_FILE=target/demo-0.0.1-SNAPSHOT.jar
WORKDIR /opt/app
COPY ${JAR_FILE} application.jar
EXPOSE 8081/tcp
EXPOSE 5005
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005
ENTRYPOINT ["java", "-jar", "application.jar"]