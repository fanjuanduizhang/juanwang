# ============================================================
# SurveyKing Railway 部署 - 多阶段 Docker 构建
# Stage 1: 编译 (Eclipse Temurin JDK 17 on Debian)
# Stage 2: 运行 (Eclipse Temurin JRE 17, 轻量)
# ============================================================

# -------- 阶段1: 编译 --------
FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /build

COPY server/ ./server/

RUN chmod +x server/gradlew

RUN cd /build/server && \
    ./gradlew :api:bootJar -Ppro --no-daemon -Dorg.gradle.jvmargs="-Xmx1024m -Xms512m"

# -------- 阶段2: 运行 --------
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=builder /build/server/api/build/libs/*.jar app.jar

RUN mkdir -p /app/files /app/logs

ENV JAVA_OPTS="-Xmx768m -Xms384m -XX:+UseG1GC"
ENV SPRING_PROFILES_ACTIVE=pro

# 完全使用 Railway 注入的 PORT，不加任何硬编码
# 打印完整环境信息用于 Deploy Logs 调试
ENTRYPOINT ["sh", "-c", \
    "echo '=== SURVEYKING START ===' && \
     echo 'PORT='$PORT && \
     echo 'RAILWAY_ENVIRONMENT='$RAILWAY_ENVIRONMENT && \
     echo 'RAILWAY_SERVICE_NAME='$RAILWAY_SERVICE_NAME && \
     echo '=== END DEBUG ===' && \
    java ${JAVA_OPTS} \
      -Dserver.address=0.0.0.0 \
      -Dserver.port=${PORT:-8080} \
      -jar app.jar"]
