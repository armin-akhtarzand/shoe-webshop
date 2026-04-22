FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

ARG GITHUB_ACTOR
ARG GH_PACKAGES_TOKEN

RUN mkdir -p /root/.m2 && printf '<settings>\n\
  <servers>\n\
    <server>\n\
      <id>github</id>\n\
      <username>%s</username>\n\
      <password>%s</password>\n\
    </server>\n\
  </servers>\n\
</settings>' "$GITHUB_ACTOR" "$GH_PACKAGES_TOKEN" > /root/.m2/settings.xml

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/shoe-webshop-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]