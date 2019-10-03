# quarkus-jpa

Here is examined how to avoid checking in db credentials into version control by using the MP configuration mechanism using system properties on application startup.

## building and running in dev mode

mvn compile quarkus:dev

## Urls

[http://localhost:8080/heartbeats](http://localhost:8080/heartbeats)

## building and running an uber jar

mvn clean package

then start with

	java -Dquarkus.datasource.url=jdbc:mysql://localhost:3306/authbv -Dquarkus.datasource.username=bv -jar target/quarkus-jpa-runner.jar

hereby overiding the application properties in application.properties what simulates a prod mode with secret db credentials.
