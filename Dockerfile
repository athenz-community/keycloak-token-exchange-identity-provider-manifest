FROM alpine:latest

COPY target/keycloak-token-provider-1.0.0.jar /plugin.jar

CMD ["cp", "/plugin.jar", "/opt/athenz/zts/lib/jars/keycloak-token-provider.jar"]
