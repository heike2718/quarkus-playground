# quarkus-playground

## good to know

application.properties that are contained inside a config dir besides the executable jar are overriding the application.properties inside the jar.

[Example](https://github.com/quarkusio/quarkus/issues/4123)

This is a much easier way to hide secrets as passwords in the git sources of the project. It makes the attempts redirect secret configurations via a secret ConfigSource the path to the file with the config parameters is contaned inside the application.properties, as it is demonstrated in SecretConfigSource, unnecessary.
