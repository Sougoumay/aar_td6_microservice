#/bin/sh
docker run -d --name=dev-consul-hashicorp -p 8500:8500 -e CONSUL_BIND_INTERFACE=eth0 hashicorp/consul