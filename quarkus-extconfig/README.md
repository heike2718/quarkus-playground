# quarkus-extconfig

1) I do need configurations that may be located outside version control because they contain secret properties.

This project shows an indirection by configuring the absolute path to the file as application property based on the default MP configuration mechanism and providing the secret properties via an application scoped bean. (see ApplicationScopedResource)

2) I do need configurations that may change when the application is running.

Those configurations are located outside the classpath in the file system so that they can easily be edited. They are provided via a similar indirection mechanism of configuring the absolute path to the properties file as application property using the MP configuration mechanism. (see GreetingResource)

## building and running

mvn compile quarkus:dev

## Urls

[http://localhost:8080/greeting](http://localhost:8080/greeting)

[http://localhost:8080/secret](http://localhost:8080/secret)

When changeable.properties changes after a refresh of the greeting-url the changes are visible.
