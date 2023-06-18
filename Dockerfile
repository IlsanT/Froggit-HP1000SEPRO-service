FROM eclipse-temurin:17.0.4.1_1-jre-focal
RUN mkdir /opt/app
COPY target/weatherstation-service-0.0.1-SNAPSHOT.jar /opt/app
CMD ["java", "-jar", "/opt/app/weatherstation-service-0.0.1-SNAPSHOT.jar"]