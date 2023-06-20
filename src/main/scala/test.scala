import io.circe.generic.auto._
import io.circe.parser._
import sttp.client3._



object test extends App {
  private val backend = HttpURLConnectionBackend()
  private val request = basicRequest.get(uri"https://wolnelektury.pl/api/books/")
  private val response = request.send(backend)

  response.body match {
    case Right(body) =>
      decode[List[Book]](body) match {
        case Right(books) =>
          val hrefs = books.map(_.href).map(_.split("/").last)
          hrefs.foreach(book => println(Data.fetchWolneLekturyBook(book)))

        case Left(error) =>
          println(s"Failed to parse JSON: $error")
      }

    case Left(error) =>
      println(s"Failed to fetch data from API: $error")
  }
}
