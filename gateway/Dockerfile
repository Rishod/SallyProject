FROM amazoncorretto:8

# set deployment directory
WORKDIR /gateway-service

# copy over the built artifact from the maven image
COPY target/gateway-*.jar ./gateway.jar

# set the startup command to run your binary
CMD ["java", "-jar", "./gateway.jar"]