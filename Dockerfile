FROM maven:3.8.5-openjdk-17 AS builder

WORKDIR /app

COPY pom.xml ./
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests && ls -la target/

FROM tomcat:9.0

WORKDIR /usr/local/tomcat/webapps/

COPY --from=builder /app/target/imagefinder-0.1.0-SNAPSHOT.war ./ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]