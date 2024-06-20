FROM maven:3.8.5-openjdk-17-slim

RUN apt update && \
    apt install -y awscli git jq gettext ssh tree
RUN curl -LO https://dl.k8s.io/release/v1.23.0/bin/linux/amd64/kubectl && \
         chmod +x ./kubectl && \
         mv ./kubectl /usr/local/bin/kubectl

ENTRYPOINT []
