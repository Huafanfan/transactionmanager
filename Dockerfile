# 使用 Maven 官方镜像构建应用
FROM maven:3.9.5-openjdk-21 AS build

# 设置工作目录
WORKDIR /app

# 将 pom.xml 和源代码复制到工作目录
COPY pom.xml .
COPY src ./src

# 构建应用
RUN mvn clean package -DskipTests

# 使用 OpenJDK 21 运行应用
FROM openjdk:21-jdk-slim

# 复制构建的 jar 文件到新的镜像中
COPY --from=build /app/target/transaction-manager-0.0.1-SNAPSHOT.jar app.jar

# 设置环境变量
ENV JAVA_OPTS=""

# 暴露应用运行的端口
EXPOSE 8080

# 启动 Java 应用程序
ENTRYPOINT ["java", "-jar", "/app.jar"]
