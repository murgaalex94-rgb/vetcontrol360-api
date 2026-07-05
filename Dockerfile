FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY . .

# Dar permisos de ejecucion a mvnw
RUN chmod +x mvnw

# Compilar
RUN ./mvnw clean package -DskipTests
EXPOSE 8080
CMD ["java", "-jar", "target/*.jar"]
