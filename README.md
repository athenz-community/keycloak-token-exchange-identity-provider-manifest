# keycloak-token-exchange-identity-provider-manifest

To create a jar plugin, do the following:

```sh
git clone git clone https://github.com/athenz-community/keycloak-token-exchange-identity-provider-manifest.git keycloak_jar
make -c keycloak_jar build
```

then:

```sh
ls -al keycloak_jar/target/keycloak*.jar

# -rw-r--r--  1 user  staff  16601809 Apr 27 16:25 target/keycloak-token-provider-1.0.0.jar
```

To write the built `.jar` as k8s configmap (aka cm)

```sh
make -c keycloak_jar deploy-as-k8s-cm

# ...
# --- Summary ----------------------
# Namespace             : athenz
# ConfigMap             : keycloak-token-provider-jar
# Athenz ZTS Deployment : skipped
# Jar File              : target/original-keycloak-token-provider-1.0.0.jar
# Restart?              : n
# ------------------------------------

# 📦 Updating ConfigMap...
# configmap/keycloak-token-provider-jar created
```

To mount the configmap created above, you need to modify the Athenz ZTS server deployment. You can do it by yourself, but you can also run the following command:

```sh
make -c keycloak_jar apply-as-k8s-cm-volume-mount
```

Then check yourself with ls command:

```sh
kubectl -n athenz exec deployment/athenz-zts-server -c athenz-zts-server -- sh -c "ls -al /opt/athenz/zts/lib/jars | grep keycloak"

# -rw-r--r-- 1 root   root      3330 Apr 27 16:55 keycloak-token-provider.jar
```

> [!NOTE]
> Make sure to have the `jwks_uri` fetchable from your server. you can always to `curl` inside your pod/server etc

Even if the jar is mounted on the Athenz server, the athenz does not use the jar file unless you let it know to trust it.

```sh
cat <<EOF | kubectl apply -f -
apiVersion: v1
kind: ConfigMap
metadata:
  name: zts-providers-config
  namespace: athenz
data:
  providers.json: |
    {
      "providers": [
        {
          "name": "keycloak-local",
          "issuer": "https://localhost:9089/realms/local-openwebui",
          "jwks_uri": "http://host.docker.internal:9090/realms/local-openwebui/protocol/openid-connect/certs",
          "provider_class": "com.mlajkim.athenz.KeycloakTokenProvider",
          "audience": "local-open-webui"
        }
      ]
    }
EOF

# configmap/zts-providers-config created
```

Then give setting

```sh
athenz.zts.oauth_provider_config_file=/opt/athenz/zts/conf/providers.json
```

![zts_properties_setting](./assets/zts_properties_setting.png)

Finally restart ZTS server:

```sh
kubectl -n athenz rollout restart deployment athenz-zts-server
```
