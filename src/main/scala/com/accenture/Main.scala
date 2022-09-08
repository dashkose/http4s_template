package com.accenture

import com.accenture.api.Api
import com.accenture.config.ApiConfig
import com.accenture.controller.LibraryController
import com.accenture.services.LibraryService
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.Router
import sttp.tapir.server.http4s.Http4sServerOptions
import sttp.tapir.server.http4s.ztapir.ZHttp4sServerInterpreter
import zio.interop.catz.*
import sttp.tapir.server.metrics.prometheus.PrometheusMetrics
import zio.*
import zio.logging.backend.SLF4J
import scala.io.StdIn

object Main extends ZIOAppDefault {

  val logger = Runtime.removeDefaultLoggers >>> SLF4J.slf4j
  private val prometeusMetric = PrometheusMetrics.default[Task]()

  override def run: ZIO[Environment with ZIOAppArgs with Scope, Any, Any] =
    application.provide(
      LibraryService.dummyLayer,
      LibraryController.layer,
      ZLayer.succeed(prometeusMetric),
      ApiConfig.fromEnv,
      logger,
      Api.layer
    )

  private val application = for {
    api <- ZIO.service[Api]
    apiConfig <- ZIO.service[ApiConfig]
    result <- buildServer(apiConfig, prometeusMetric, api)
  } yield result

  private def buildServer(apiConfig: ApiConfig, prometheusMetrics: PrometheusMetrics[Task], api: Api): RIO[Any, Unit] = {
    val serverOptions: Http4sServerOptions[Task] =
      Http4sServerOptions
        .customiseInterceptors[Task]
        .metricsInterceptor(prometheusMetrics.metricsInterceptor())
        .options
    val routes = ZHttp4sServerInterpreter(serverOptions).from(api.allEndpoints).toRoutes

    ZIO.executor.flatMap { executor =>
      BlazeServerBuilder[Task]
        .withExecutionContext(executor.asExecutionContext)
        .bindHttp(apiConfig.port, apiConfig.host)
        .withHttpApp(Router("/" -> routes).orNotFound)
        .serve
        .compile
        .drain
    }
  }

}
