package ae.maf.api

import sttp.client3.testing.SttpBackendStub
import sttp.client3.ziojson.*
import sttp.client3.{Response, ResponseException, UriContext, basicRequest}
import sttp.tapir.server.metrics.prometheus.PrometheusMetrics
import sttp.tapir.server.stub.TapirStubInterpreter
import sttp.tapir.ztapir.RIOMonadError
import zio.*
import zio.test.*
import zio.test.Assertion.*
import zio.test.{Spec, TestEnvironment, ZIOSpecDefault, assertTrue, assertZIO}

import ae.maf.api.protocol.library.Book
import ae.maf.api.protocol.library.{Author, Book}
import ae.maf.controller.LibraryController
import ae.maf.services.LibraryService

object ApiSpec extends ZIOSpecDefault {

  private val prometeusMetric = PrometheusMetrics.default[Task]()
  val apiLayer = ((LibraryService.dummyLayer >>> LibraryController.layer) ++ ZLayer.succeed(prometeusMetric)) >>> Api.layer

  override def spec: Spec[TestEnvironment, Any] = {
    specLayered.provideSomeLayerShared(apiLayer)
  }

  def specLayered: Spec[TestEnvironment with Api, Any] = {
    suite("Api should")(
      test("list available books") {
        for {
          api <- ZIO.service[Api]
          backendStub = TapirStubInterpreter(SttpBackendStub(new RIOMonadError[Any]))
            .whenServerEndpoint(api.booksListingServerEndpoint)
            .thenRunLogic()
            .backend()
          response <- basicRequest
            .get(uri"http://localhost/books")
            .response(asJson[List[Book]])
            .send(backendStub)
        } yield assert(response.body.getOrElse(List.empty))(equalTo(books))

      }
    )
  }

  val books = List(
    Book("The Sorrows of Young Werther", 1774, Author("Johann Wolfgang von Goethe")),
    Book("On the Niemen", 1888, Author("Eliza Orzeszkowa")),
    Book("The Art of Computer Programming", 1968, Author("Donald Knuth")),
    Book("Pharaoh", 1897, Author("Boleslaw Prus"))
  )
}
