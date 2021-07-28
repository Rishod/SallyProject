FROM amazoncorretto:8

# set deployment directory
WORKDIR /shop-service

# copy over the built artifact from the maven image
COPY target/shop-service-*.jar ./shop-service.jar

# set the startup command to run your binary
CMD ["java", "-jar", "./shop-service.jar"]