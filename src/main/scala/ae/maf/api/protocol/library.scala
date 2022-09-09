package ae.maf.api.protocol

import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}

import ae.maf.domain.library as domain

object library {
  case class Author(name: String)
  object Author {
    given authorEncoder: JsonEncoder[Author] = DeriveJsonEncoder.gen[Author]
    given authorDecoder: JsonDecoder[Author] = DeriveJsonDecoder.gen[Author]

    def fromDomain(dAuthor: domain.Author): Author = Author(dAuthor.name)
  }
  case class Book(title: String, year: Int, author: Author)
  object Book {
    given bookEncoder: JsonEncoder[Book] = DeriveJsonEncoder.gen[Book]
    given bookDecoder: JsonDecoder[Book] = DeriveJsonDecoder.gen[Book]

    def fromDomain(dBook: domain.Book): Book = Book(dBook.title, dBook.year, Author.fromDomain(dBook.author))
  }
}
