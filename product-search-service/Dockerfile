FROM amazoncorretto:8

# set deployment directory
WORKDIR /product-search-service

# copy over the built artifact from the maven image
COPY target/product-search-service-*.jar ./product-search-service.jar

# set the startup command to run your binary
CMD ["java", "-jar", "./product-search-service.jar"]