FROM amazoncorretto:8

# set deployment directory
WORKDIR /user-service

# copy over the built artifact from the maven image
COPY target/user-service-*.jar ./user-service.jar

# set the startup command to run your binary
CMD ["java", "-jar", "./user-service.jar"]