# keycloak-token-exchange-identity-provider-manifest

To create a jar plugin, do the following:

```sh
make build
```

then:

```sh
ls -al target/keycloak*.jar

# -rw-r--r--  1 user  staff  16601809 Apr 27 16:25 target/keycloak-token-provider-1.0.0.jar
```

to write the built `.jar` as k8s configmap (aka cm)

```sh
make deploy-as-k8s-cm

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

