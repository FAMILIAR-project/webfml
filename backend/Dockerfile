FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

# Copy Maven files
COPY pom.xml .
COPY src ./src

# Build the application
RUN apk add --no-cache maven && \
    mvn clean package -DskipTests && \
    mv target/*.jar app.jar

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the built JAR from build stage
COPY --from=build /app/app.jar .

# Create workspace directory
RUN mkdir -p /app/workspace

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
