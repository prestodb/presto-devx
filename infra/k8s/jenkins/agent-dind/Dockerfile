FROM docker:27.0.2-dind-alpine3.20

RUN apk update && apk upgrade && apk add aws-cli bash git make && \
    git config --global --add safe.directory '*'
