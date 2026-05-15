# ============================================================
# SurveyKing Railway 部署 - 多阶段 Docker 构建
# Stage 1: 编译 (JDK 17 + Gradle)
# Stage 2: 运行 (JRE 17, 轻量)
# ============================================================

# -------- 阶段1: 编译 --------
FROM eclipse-temurin:17-jdk-alpine AS builder

# 安装 Gradle 依赖（Alpine 没有 glibc，需要一些工具）
RUN apk add --no-cache bash curl git

WORKDIR /build

# 先复制 Gradle wrapper 文件（利用 Docker 缓存层）
COPY server/gradlew server/gradlew.bat server/gradle/ ./server/
COPY server/build.gradle server/settings.gradle ./server/
COPY server/shared/ ./server/shared/
COPY server/rdbms/ ./server/rdbms/
COPY server/flow/ ./server/flow/
COPY server/exam/ ./server/exam/
COPY server/ai/ ./server/ai/
COPY server/api/ ./server/api/

# 赋予执行权限
RUN chmod +x server/gradlew

# 执行 Gradle 构建（生产模式 -Ppro）
RUN cd server && ./gradlew bootJar -Ppro --no-daemon

# -------- 阶段2: 运行 --------
FROM eclipse-temurin:17-jre-alpine

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
