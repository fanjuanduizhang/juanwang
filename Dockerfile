# ============================================================
# SurveyKing Railway 部署 - 多阶段 Docker 构建（优化缓存）
# Stage 1: 编译 (Eclipse Temurin JDK 17 on Debian)
# Stage 2: 运行 (Eclipse Temurin JRE 17, 轻量)
# ============================================================

# -------- 阶段1: 编译 --------
FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /build

# 先只复制 Gradle Wrapper（变化频率低，可复用缓存）
COPY server/gradlew server/gradlew.bat server/gradle/wrapper/ /build/server/

RUN chmod +x /build/server/gradlew && \
    cd /build/server && \
    ./gradlew --version --no-daemon 2>/dev/null || true

# 再复制完整源码（变化频率高，单独一层）
COPY server/ ./server/

RUN chmod +x /build/server/gradlew && \
    cd /build/server && \
    ./gradlew :api:bootJar -Ppro --no-daemon -Dorg.gradle.jvmargs="-Xmx1024m -Xms512m"

# -------- 阶段2: 运行 --------
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=builder /build/server/api/build/libs/*.jar app.jar

RUN mkdir -p /app/files /app/logs

ENV JAVA_OPTS="-Xmx768m -Xms384m -XX:+UseG1GC"
ENV SPRING_PROFILES_ACTIVE=pro

# Railway 注入 PORT=8080，应用监听此端口
ENTRYPOINT ["sh", "-c", \
    "java ${JAVA_OPTS} \
      -Dserver.address=0.0.0.0 \
      -Dserver.port=${PORT:-8080} \
      -jar app.jar"]
