## Scala http4s server template
Uses Scala 3, http4s web server, ZIO, Tapir.
Uses ZIO Layers for dependency injection.
Has dummy service to get list of books and controller, api to serve this request.
Integrates Swagger page that updates automatically whenever api route added.
Integrates Prometeus metrics as example.
Can be replaced with any metric Tapir support [Observability](https://tapir.softwaremill.com/en/latest/server/observability.html)

### Exposed urls
/books - return list of books from the dummy library service
/docs - Swagger page for a service with all the api endpoints. 
/metrics - Metrics for the service

### Quick start
If you don't have [sbt](https://www.scala-sbt.org) installed already, you can use the provided wrapper script:

```shell
./sbtx test # run the tests
./sbtx run # run the application (Main)
```

For more details check the [sbtx usage](https://github.com/dwijnand/sbt-extras#sbt--h) page.

Otherwise, if sbt is already installed, you can use the standard commands:

```shell
sbt test # run the tests
sbt run # run the application (Main)
```

### To run application locally
You should set HTTP_HOST and HTTP_PORT environment variables.
If you are on *nix system you can use
```shell
source .envrc
```
to export environment variables before running application.
Then you can run normal `sbt run` command
Http server will start on the port specified.

### To run application in Docker
You can execute command `sbt publishLocal` to publish Docker image locally.
You should set HTTP_HOST and HTTP_PORT environment variables.
If you are on *nix system you can use
```shell
source .envrc
```
to export environment variables before running application.
Then bring it up with a command `docker-compose -f ./docker/docker-compose.yml up`
Http server will start on the port specified.

### Useful commands
* Format source code:
```shell
sbt fmtAll
```
* Run tests:
```shell
sbt test
```

### Links:

* [sbtx wrapper](https://github.com/dwijnand/sbt-extras#installation)
