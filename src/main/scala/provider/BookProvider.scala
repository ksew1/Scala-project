package provider

import traits.Provider
import io.circe.generic.auto._
import io.circe.parser.decode
import sttp.client3._
import utils.Book

object BookProvider extends Provider {
  def fetchWolneLekturyBooks(): Option[List[String]] = {
    val response = sendRequest()
    parseResponseBody(response)
  }

  private def sendRequest(): Response[Either[String, String]] = {
    val backend = HttpURLConnectionBackend()
    val request = basicRequest.get(uri"https://wolnelektury.pl/api/books/")
    request.send(backend)
  }

  private def parseResponseBody(response: Response[Either[String, String]]): Option[List[String]] = {
    response.body match {
      case Right(body) => decodeJson(body)
      case Left(error) =>
        println(s"Failed to fetch data from API: $error")
        None
    }
  }

  private def decodeJson(json: String): Option[List[String]] = {
    decode[List[Book]](json) match {
      case Right(books) => Some(processBooks(books))
      case Left(error) =>
        println(s"Failed to parse JSON: $error")
        None
    }
  }

  private def processBooks(books: List[Book]): List[String] = {
    books.map(_.href).map(_.split("/").filter(_.nonEmpty).last)
  }

  def fetchWolneLekturyBook(title: String): String = {
    val url = s"https://wolnelektury.pl/media/book/txt/$title.txt"
    cleanText(removeWolneLekturyFooter(fetchFromURL(url)))
  }

  private def removeWolneLekturyFooter(text: String): String = {
    val footerStart = text.indexOf("-----")
    if (footerStart != -1) text.substring(0, footerStart).trim else text
  }

}
