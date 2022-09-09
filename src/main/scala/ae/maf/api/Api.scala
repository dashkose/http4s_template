package ae.maf.api

import spec.booksListing
import sttp.tapir.server.metrics.prometheus.PrometheusMetrics
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import sttp.tapir.ztapir._
import zio.*

import ae.maf.api.protocol.{errors, library}
import ae.maf.controller.LibraryController

trait Api {
  val apiEndpoints: List[ZServerEndpoint[Any, Any]]
  val allEndpoints: List[ZServerEndpoint[Any, Any]]
  private[api] val booksListingServerEndpoint: ZServerEndpoint[Any, Any]
}

object Api {
  class ApiImp(libraryController: LibraryController, prometheusMetrics: PrometheusMetrics[Task]) extends Api {

    val booksListingServerEndpoint: ZServerEndpoint[Any, Any] = booksListing.zServerLogic(_ => libraryController.getBooks)

    val apiEndpoints: List[ZServerEndpoint[Any, Any]] = List(booksListingServerEndpoint)

    val docEndpoints: List[ZServerEndpoint[Any, Any]] = SwaggerInterpreter()
      .fromServerEndpoints[Task](apiEndpoints, "zio_http4s", "1.0.0")

    val metricsEndpoint: ZServerEndpoint[Any, Any] = prometheusMetrics.metricsEndpoint

    val allEndpoints: List[ZServerEndpoint[Any, Any]] = apiEndpoints ++ docEndpoints ++ List(metricsEndpoint)
  }

  val layer: ZLayer[PrometheusMetrics[Task] with LibraryController, Nothing, Api] = ZLayer(
    for {
      libController <- ZIO.service[LibraryController]
      prometheusMetrics <- ZIO.service[PrometheusMetrics[Task]]
    } yield new ApiImp(libController, prometheusMetrics)
  )
}
