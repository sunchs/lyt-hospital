FROM registry.cn-shenzhen.aliyuncs.com/sunchs/lyt:java-system-v1.0

ADD question-service/target/question-service.jar /app.jar

ENTRYPOINT [ "sh", "-c", "java -Dfile.encoding=UTF8 -Duser.timezone=GMT+08 -jar /app.jar" ]