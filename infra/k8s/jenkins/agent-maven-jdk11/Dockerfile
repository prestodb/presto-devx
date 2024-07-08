FROM maven:3.8.6-openjdk-11-slim

RUN apt update && apt install -y awscli bash curl git jq && \
    apt clean && \
    git config --global --add safe.directory '*'
