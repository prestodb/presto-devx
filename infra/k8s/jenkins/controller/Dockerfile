ARG JENKINS_VERSION
FROM jenkins/jenkins:${JENKINS_VERSION}

COPY --chown=jenkins:jenkins plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN jenkins-plugin-cli -f /usr/share/jenkins/ref/plugins.txt
