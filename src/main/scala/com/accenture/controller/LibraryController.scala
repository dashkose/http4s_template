package com.accenture.controller

import com.accenture.services.LibraryService
import com.accenture.services.LibraryService.errors
import com.accenture.api.protocol.errors.GeneralError
import com.accenture.api.protocol.library.Book
import zio.*

trait LibraryController {
  def getBooks: IO[GeneralError, List[Book]]
}

object LibraryController {
  class LibraryControllerImp(libraryService: LibraryService) extends LibraryController {
    def getBooks: IO[GeneralError, List[Book]] = libraryService.getBooks.mapBoth(
      { case errors.BusinessError(message) =>
        GeneralError(message)
      },
      _.map(Book.fromDomain)
    )
  }

  val layer: ZLayer[LibraryService, Nothing, LibraryController] = ZLayer(for {
    service <- ZIO.service[LibraryService]
  } yield new LibraryControllerImp(service))
}
