# ---- Stage 1: Build ----
# 使用一个包含了 Maven 3.9 和 Java 21 (Temurin) 的官方镜像作为构建环境。
# 我们给这个阶段起一个别名 "builder"，方便后面引用。
FROM maven:3.9-eclipse-temurin-21 AS builder

# 在这个构建环境的容器内，创建一个名为 /app 的工作目录。
WORKDIR /app

# 将项目根目录下的 pom.xml 文件复制到容器的 /app 目录中。
COPY pom.xml .

# 运行 Maven 命令，预先下载所有项目依赖。
# 这样做可以利用 Docker 的缓存机制：只要 pom.xml 不变，下次构建时就无需重新下载，从而加快速度。
RUN mvn dependency:go-offline

# 将项目的所有源代码 (src 文件夹) 复制到容器的 /app/src 目录中。
COPY src ./src

# 运行 Maven 命令来打包项目，生成 .jar 文件。
# -DskipTests 表示跳过测试，以加快构建速度。
RUN mvn package -DskipTests
# ---- Stage 2: Run ----
# 使用一个仅包含 Java 21 运行时环境 (JRE) 的镜像。
# JRE 比 JDK 小得多，因为它不包含编译器等开发工具，非常适合生产环境。
FROM eclipse-temurin:21-jre

# 同样在容器内创建一个 /app 工作目录。
WORKDIR /app

# 这是最关键的一步：从第一阶段 (builder) 的容器里，
# 将构建好的 .jar 文件 (/app/target/*.jar) 复制到当前容器的 /app 目录，并重命名为 app.jar。
COPY --from=builder /app/target/*.jar app.jar

# 声明容器将会在 8080 端口上监听网络连接。
# 这只是一个元数据声明，实际的端口映射在 docker-compose.yml 中完成。
EXPOSE 8080

# 定义容器启动时要执行的命令。
# 这里是使用 java -jar 命令来运行我们的应用程序。
ENTRYPOINT ["java", "-jar", "app.jar"]