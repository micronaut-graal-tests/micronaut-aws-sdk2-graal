# Micronaut AWS Parameter Store GraalVM

Test application for Micronaut AWS SDKv2 and GraalVM that uses Parameter Store client.

To test the application:

1. Build application `./build-native-image.sh`
2. Setup AWS credentials
3. Call api:

```
curl -X POST -H "Content-Type: application/json"  -d '{"name":"/foo/bar", "type":"String", "value":"micronaut"}' http://localhost:8080/paramStore/
curl -X GET localhost:8080/paramStore
curl -X DELETE localhost:8080/paramStore/foo/bar
```
