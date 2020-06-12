# resilience4j-demo
Demo Project for Resilience4j library based on Kotlin.

See https://resilience4j.readme.io/ for Resilience4j library.

1. https://resilience4j.readme.io/docs/circuitbreaker
2. https://resilience4j.readme.io/docs/ratelimiter

### Run the application
```
$ mvn spring-boot:run
```

### Run tests
```
$ mvn test
```
See Test classes and api-requests.sh to call endpoints.

#### TODO
Would be nice to implement Bulkhead and Retry.

1. https://resilience4j.readme.io/docs/bulkhead
2. https://resilience4j.readme.io/docs/retry