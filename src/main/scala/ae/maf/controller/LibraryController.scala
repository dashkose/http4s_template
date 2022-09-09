package ae.maf.controller

import zio.*

import ae.maf.api.protocol.errors.GeneralError
import ae.maf.api.protocol.library.Book
import ae.maf.services.LibraryService
import ae.maf.services.LibraryService.errors.BusinessError

trait LibraryController {
  def getBooks: IO[GeneralError, List[Book]]
}

object LibraryController {
  class LibraryControllerImp(libraryService: LibraryService) extends LibraryController {
    def getBooks: IO[GeneralError, List[Book]] = libraryService.getBooks.mapBoth(
      { case BusinessError(message) =>
        GeneralError(message)
      },
      _.map(Book.fromDomain)
    )
  }

  val layer: ZLayer[LibraryService, Nothing, LibraryController] = ZLayer(for {
    service <- ZIO.service[LibraryService]
  } yield new LibraryControllerImp(service))
}
