# Micronaut AWS S3 GraalVM

Test application for Micronaut AWS SDKv2 and GraalVM that uses S3 client.

To test the application:

1. Build application `./gradlew nativeImage`
2. Setup AWS credentials
3. Call api:

```
curl -X POST localhost:8080/s3/buckets/someuniquebucketname
curl -X GET localhost:8080/s3/buckets
curl -X DELETE localhost:8080/s3/buckets/someuniquebucketname
```
