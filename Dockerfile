### Build stage.
## To build specific service do next step:
## 1. Run docker image build --target build -t sally-build .
## 2. Rundocker image build --target <chosen-service> -t <your-tag> .
FROM maven:3.8.3-amazoncorretto-8 as build

# copy your other files
COPY . ./

# build without running tests
RUN mvn install -Dmaven.test.skip=true

### USER-SERVICE
FROM amazoncorretto:8 AS user-service

# set deployment directory
WORKDIR /user-service

# copy over the built artifact from the maven image
COPY --from=sally-build:latest user-service/target/user-service-*.jar ./user-service.jar

# set the startup command to run your binary
CMD ["java", "-jar", "./user-service.jar"]

### GATEWAY
FROM amazoncorretto:8 AS gateway

# set deployment directory
WORKDIR /gateway-service

# copy over the built artifact from the maven image
COPY --from=sally-build:latest gateway/target/gateway-*.jar ./gateway.jar

# set the startup command to run your binary
CMD ["java", "-jar", "./gateway.jar"]

### ORDER-SERVICE
FROM amazoncorretto:8 AS order-service

# set deployment directory
WORKDIR /order-service

# copy over the built artifact from the maven image
COPY --from=sally-build:latest order-service/target/order-service-*.jar ./order-service.jar

# set the startup command to run your binary
CMD ["java", "-jar", "./order-service.jar"]

### PRODUCT-SEARCH-SERVICE
FROM amazoncorretto:8 AS product-search-service

# set deployment directory
WORKDIR /product-search-service

# copy over the built artifact from the maven image
COPY --from=sally-build:latest product-search-service/target/product-search-service-*.jar ./product-search-service.jar

# set the startup command to run your binary
CMD ["java", "-jar", "./product-search-service.jar"]

### SHOP-SERVICE
FROM amazoncorretto:8 AS shop-service

# set deployment directory
WORKDIR /shop-service

# copy over the built artifact from the maven image
COPY --from=sally-build:latest shop-service/target/shop-service-*.jar ./shop-service.jar

# set the startup command to run your binary
CMD ["java", "-jar", "./shop-service.jar"]