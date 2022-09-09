package ae.maf.api

import sttp.tapir.*
import sttp.tapir.generic.auto.*
import sttp.tapir.json.zio.*
import sttp.tapir.json.zio.jsonBody
import sttp.tapir.{PublicEndpoint, endpoint}

import ae.maf.api.protocol.errors.GeneralError
import ae.maf.api.protocol.library.Book

object spec {
  val booksListing: PublicEndpoint[Unit, GeneralError, List[Book], Any] = endpoint.get
    .in("books")
    .errorOut(jsonBody[GeneralError])
    .out(jsonBody[List[Book]])

}
