build-n-deploy: build deploy-as-k8s-cm

build:
	@mvn clean package \
		-Dmaven.resolver.transport=wagon \
		-Dmaven.wagon.http.ssl.insecure=true \
		-Dmaven.wagon.http.ssl.allowall=true

deploy-as-k8s-cm:
	@chmod +x hack/deploy-as-k8s-cm.sh
	@./hack/deploy-as-k8s-cm.sh

apply-as-k8s-cm-volume-mount:
	@kubectl patch deployment athenz-zts-server -n athenz --patch-file hack/static/zts-plugin-volume-mount.yaml