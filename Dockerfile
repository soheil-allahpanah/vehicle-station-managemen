FROM amazoncorretto:17-alpine3.13-jdk
EXPOSE 8080
ADD target/vehicle-station-management-1.0.0.jar vehicle-station-management.jar
ENTRYPOINT ["java", "-jar","vehicle-station-management.jar"]
