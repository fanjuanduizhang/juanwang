# 使用 Eclipse Temurin JDK 17（轻量级，适合容器）
FROM eclipse-temurin:17-jre-alpine

# 设置工作目录
WORKDIR /app

# 复制 JAR 文件
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# 创建必要的目录
RUN mkdir -p /app/files /app/logs

# 设置环境变量
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SPRING_PROFILES_ACTIVE=pro

# 暴露端口（Railway 会通过 $PORT 覆盖）
EXPOSE 8080

# 持久化卷声明
VOLUME ["/app/files", "/app/logs"]

# 启动命令
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -Dserver.port=${PORT:-8080} -jar app.jar ${0} ${@}"]
