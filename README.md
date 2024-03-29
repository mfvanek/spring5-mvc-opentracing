# spring5-mvc-opentracing
Spring 5 MVC demo app with embedded Jetty (without Spring Boot!) and OpenTracing

[![Java CI](https://github.com/mfvanek/spring5-mvc-opentracing/actions/workflows/tests.yml/badge.svg)](https://github.com/mfvanek/spring5-mvc-opentracing/actions/workflows/tests.yml)

## Open in Browser
* http://localhost:8080/api/v1/demo
* http://localhost:8080/api/v1/demoException
* http://localhost:8080/api/v1/welcome

## Swagger
* http://localhost:8080/v2/api-docs
* http://localhost:8080/swagger-ui/
* http://localhost:8080/swagger-ui/index.html

## Run in Docker
```
docker run --name spring5-mvc-opentracing -d -p 8080:8080 localhost:5000/spring5-mvc-opentracing:1.0-SNAPSHOT
```

## Run Jaeger in Docker
```
docker run -d --name jaeger \
  -e COLLECTOR_ZIPKIN_HOST_PORT=:9411 \
  -p 5775:5775/udp \
  -p 6831:6831/udp \
  -p 6832:6832/udp \
  -p 5778:5778 \
  -p 16686:16686 \
  -p 14250:14250 \
  -p 14268:14268 \
  -p 14269:14269 \
  -p 9411:9411 \
  jaegertracing/all-in-one:1.32
```

Jaeger UI will start at `http://localhost:16686`

## Useful Commands

```
# Create uber jar
mvn clean install

# Run uber jar
java -jar target/spring5-mvc-opentracing-jar-with-dependencies.jar
```

## Build Docker image

`mvn clean package docker:build`
