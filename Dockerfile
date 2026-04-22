FROM eclipse-temurin:21
LABEL mentainer="kkunal2005@gmail.com"
WORKDIR /app
COPY target/api-0.0.1-SNAPSHOT.jar /app/api.jar
ENTRYPOINT ["java" , "-jar", "-Xmx2048m", "-XX:MaxMetaspaceSize=512m", "api.jar"]
