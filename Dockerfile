# ============================================================
# SurveyKing Railway 部署 - 多阶段 Docker 构建
# Stage 1: 编译 (Eclipse Temurin JDK 17 on Debian)
# Stage 2: 运行 (Eclipse Temurin JRE 17, 轻量)
# ============================================================

# -------- 阶段1: 编译 --------
FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /build

# 先复制 Gradle wrapper 和构建配置（利用 Docker 缓存层）
COPY server/gradlew server/gradlew.bat server/gradle/ ./server/
COPY server/build.gradle server/settings.gradle ./server/

# 复制所有子模块源码
COPY server/shared/ ./server/shared/
COPY server/rdbms/ ./server/rdbms/
COPY server/flow/ ./server/flow/
COPY server/exam/ ./server/exam/
COPY server/ai/ ./server/ai/
COPY server/api/ ./server/api/

# 赋予执行权限
RUN chmod +x server/gradlew

# 执行 Gradle 构建（生产模式 -Ppro）
# 限制 JVM 内存避免 OOM，关闭守护进程
RUN cd server && \
    chmod +x gradlew && \
    ./gradlew bootJar -Ppro --no-daemon --parallel -Dorg.gradle.jvmargs="-Xmx1024m -Xms512m"

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
