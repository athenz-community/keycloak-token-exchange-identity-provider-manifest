FROM alpine:latest

COPY target/keycloak-token-provider-1.0.0.jar /plugin.jar

CMD ["cp", "/plugin.jar", "/export/keycloak-token-provider.jar"]
