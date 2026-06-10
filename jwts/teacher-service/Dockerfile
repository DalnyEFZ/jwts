FROM swr.cn-north-4.myhuaweicloud.com/ddn-k8s/docker.io/openjdk:8-jre
LABEL maintainer=me

ENV JAVA_ARGS="-Dfile.encoding=UTF8 -Duser.timezone=GMT+08"
ENV JAVA_OPTS=""
ENV JAR_FILE="teacher-service.jar"

COPY ./${JAR_FILE} /${JAR_FILE}

EXPOSE 9002

CMD java ${JAVA_ARGS} ${JAVA_OPTS} -jar /${JAR_FILE}
