# Stage 1: Build ứng dụng với Maven
FROM maven:3.8.7-openjdk-17-slim AS build
WORKDIR /app

# Copy file cấu hình và mã nguồn
COPY pom.xml .
COPY src ./src

# Build ứng dụng và đóng gói thành file JAR (bỏ qua test nếu muốn)
RUN mvn clean package -DskipTests

# Stage 2: Tạo image chạy ứng dụng
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Copy file JAR từ stage build (đảm bảo tên file JAR khớp với tên tạo ra)
COPY --from=build /app/target/CosmeticsStore.jar CosmeticsStore.jar

# Mở cổng 8080 (điều chỉnh theo cổng mà ứng dụng của bạn sử dụng)
EXPOSE 8080

# Chạy ứng dụng
CMD ["java", "-jar", "CosmeticsStore.jar"]
