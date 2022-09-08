package com.accenture.services

import com.accenture.domain.library.*
import com.accenture.services.LibraryService.errors.LibraryError
import zio.*

trait LibraryService {
  def getBooks: ZIO[Any, LibraryError, List[Book]]
}

object LibraryService {
  class DummyLibraryService extends LibraryService {
    def getBooks: ZIO[Any, LibraryError, List[Book]] = ZIO.succeed(
      List(
        Book("The Sorrows of Young Werther", 1774, Author("Johann Wolfgang von Goethe", 1), 1),
        Book("On the Niemen", 1888, Author("Eliza Orzeszkowa", 2), 2),
        Book("The Art of Computer Programming", 1968, Author("Donald Knuth", 3), 3),
        Book("Pharaoh", 1897, Author("Boleslaw Prus", 4), 4)
      )
    )
  }

  val dummyLayer: ULayer[LibraryService] = ZLayer.succeed(new DummyLibraryService)

  object errors {
    sealed trait LibraryError
    case class BusinessError(message: String) extends LibraryError
  }
}
