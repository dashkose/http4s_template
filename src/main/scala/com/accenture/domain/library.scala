package com.accenture.domain

object library {
  case class Author(name: String, id: Long)
  case class Book(title: String, year: Int, author: Author, id: Long)
}
