FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY . .
# Quitar carriage returns de Windows (por si acaso) y dar permisos
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

# Compilar
RUN ./mvnw clean package -DskipTests

EXPOSE 8080
# Formato shell para que el wildcard funcione
CMD java -jar target/*.jar
