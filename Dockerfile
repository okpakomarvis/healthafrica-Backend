# Multi-stage build for HealthAfrica API (staging / Render)
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /workspace

COPY pom.xml .
COPY src ./src

RUN apk add --no-cache maven \
    && mvn -q -DskipTests package \
    && mv target/public-health-portal-*.jar /workspace/app.jar

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN apk add --no-cache wget \
    && addgroup -S app && adduser -S app -G app

COPY --from=build /workspace/app.jar /app/app.jar
USER app

ENV SPRING_PROFILES_ACTIVE=staging
ENV PORT=8080
EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=5 \
  CMD wget -qO- "http://localhost:${PORT}/actuator/health" || exit 1

ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT} -jar /app/app.jar"]
