FROM docker:27.0.3-dind-alpine3.20

RUN apk update && apk upgrade && apk add --no-cache aws-cli bash git make mandoc && \
    git config --global --add safe.directory '*'
