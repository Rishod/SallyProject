FROM amazoncorretto:8

# set deployment directory
WORKDIR /order-service

# copy over the built artifact from the maven image
COPY target/order-service-*.jar ./order-service.jar

# set the startup command to run your binary
CMD ["java", "-jar", "./order-service.jar"]