# Usa a imagem do Maven para compilar o projeto
FROM maven:3.8.4-openjdk-21 AS build
WORKDIR /app

# Copia os arquivos do projeto para a imagem
COPY pom.xml .
COPY src ./src

# Executa o Maven para gerar o .jar
RUN mvn clean package -DskipTests

# Usa uma imagem menor para rodar o jar gerado
FROM openjdk:21-jdk-slim
WORKDIR /app

# Copia o jar gerado na etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]