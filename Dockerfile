FROM eclipse-temurin:17-jdk-jammy
ARG PROJECT_VERSION
# 작성자 및 메타데이터 추가
LABEL maintainer="yonghwankim.dev@gmail.com" \
      version=$PROJECT_VERSION \
      description="Invest72 Application - Spring Boot App" \
      created="2025-11-26" \
      org.opencontainers.image.source="https://github.com/yonghwankim-dev/invest72" \
      org.opencontainers.image.documentation="https://hub.docker.com/repository/docker/nemo1107/invest72-app/general" \
      org.opencontainers.image.licenses="MIT"
WORKDIR /app
COPY ./build/libs/invest72-*.jar ./invest72.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "invest72.jar"]
