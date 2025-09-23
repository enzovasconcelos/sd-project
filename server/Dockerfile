# -------------------------
# Etapa 1: Build do projeto
# -------------------------
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# Copia o pom.xml e baixa dependências
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o código-fonte
COPY src ./src

# Compila e gera o JAR "shaded" com todas as dependências, sem rodar testes
RUN mvn clean package -DskipTests

# -------------------------
# Etapa 2: Imagem final leve
# -------------------------
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copia o JAR "shaded" do builder
COPY --from=builder /app/target/sd-project-1.0-SNAPSHOT.jar app.jar

# Comando para rodar o JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
