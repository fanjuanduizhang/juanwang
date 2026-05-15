# ============================================================
# SurveyKing Railway 部署 - 多阶段 Docker 构建
# Stage 1: 编译 (Eclipse Temurin JDK 17 on Debian)
# Stage 2: 运行 (Eclipse Temurin JRE 17, 轻量)
# ============================================================

# -------- 阶段1: 编译 --------
FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /build

# 复制整个 server 目录（确保 gradle-wrapper.jar 等所有文件都进去）
COPY server/ ./server/

# 赋予执行权限
RUN chmod +x server/gradlew

# 执行 Gradle 构建（生产模式 -Ppro）
RUN cd /build/server && \
    ./gradlew bootJar -Ppro --no-daemon -Dorg.gradle.jvmargs="-Xmx1024m -Xms512m"

# -------- 阶段2: 运行 --------
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# 从编译阶段复制 JAR
COPY --from=builder /build/server/api/build/libs/*.jar app.jar

# 创建必要目录
RUN mkdir -p /app/files /app/logs

# 环境变量
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SPRING_PROFILES_ACTIVE=pro

# 启动命令：使用 Railway 注入的 $PORT
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -Dserver.port=${PORT:-8080} -jar app.jar"]
