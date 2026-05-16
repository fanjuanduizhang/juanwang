# ============================================================
# SurveyKing Railway 部署 - 多阶段 Docker 构建
# Stage 1: 编译 (Eclipse Temurin JDK 17 on Debian)
# Stage 2: 运行 (Eclipse Temurin JRE 17, 轻量)
# ============================================================

# -------- 阶段1: 编译 --------
FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /build

# 复制整个 server 目录（确保所有文件完整）
COPY server/ ./server/

# 赋予执行权限
RUN chmod +x server/gradlew

# 在 api 子模块目录下执行 Gradle 构建（bootJar 在 api 模块定义）
RUN cd /build/server && \
    ./gradlew :api:bootJar -Ppro --no-daemon -Dorg.gradle.jvmargs="-Xmx1024m -Xms512m"

# -------- 阶段2: 运行 --------
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# 从编译阶段复制 JAR
COPY --from=builder /build/server/api/build/libs/*.jar app.jar

# 创建必要目录
RUN mkdir -p /app/files /app/logs

# 环境变量（Railway 免费实例约 1GB 内存）
ENV JAVA_OPTS="-Xmx768m -Xms384m -XX:+UseG1GC"
ENV SPRING_PROFILES_ACTIVE=pro

# 启动命令：
# Railway 自动注入 $PORT 环境变量，应用必须监听此端口
# 打印 PORT 值便于调试（可在 Deploy Logs 中查看）
ENTRYPOINT ["sh", "-c", "echo \"=== Railway PORT=$PORT ===\" && java ${JAVA_OPTS} -Dserver.port=${PORT:-8080} -jar app.jar"]
