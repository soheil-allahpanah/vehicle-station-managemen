FROM amazoncorretto:17-alpine3.13-jdk
EXPOSE 8080
ADD build/libs/vehicle-station-management-1.0.jar vehicle-station-management.jar
ENTRYPOINT ["java", "-jar","vehicle-station-management.jar"]
