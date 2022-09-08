package com.accenture.api

import com.accenture.api.protocol.errors.GeneralError
import com.accenture.api.protocol.library.Book
import sttp.tapir.json.zio.jsonBody
import sttp.tapir.{PublicEndpoint, endpoint}
import sttp.tapir.generic.auto.*
import sttp.tapir.json.zio.*
import sttp.tapir.*

object spec {
  val booksListing: PublicEndpoint[Unit, GeneralError, List[Book], Any] = endpoint.get
    .in("books")
    .errorOut(jsonBody[GeneralError])
    .out(jsonBody[List[Book]])

}
