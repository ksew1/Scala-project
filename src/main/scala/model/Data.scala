package model

import org.apache.commons.text.StringEscapeUtils
import io.circe.generic.auto._
import io.circe.parser.decode
import org.json4s._
import org.json4s.native.JsonMethods._
import org.jsoup.Jsoup
import sttp.client3.{HttpURLConnectionBackend, _}

import scala.io.Source
import scala.util.Using

object Data {
  private def fetchFromURL(url: String): String = {

    Using(Source.fromURL(url)) { source =>
      source.mkString

    }.getOrElse("")
  }

  def fetchWolneLekturyBooks(): Option[List[String]] = {
    val backend = HttpURLConnectionBackend()
    val request = basicRequest.get(uri"https://wolnelektury.pl/api/books/")
    val response = request.send(backend)

    response.body match {
      case Right(body) =>
        decode[List[Book]](body) match {
          case Right(books) => {
            Some(books.map(_.href)
              .map(_.split("/").filter(_.nonEmpty).last)
              .map(book => fetchWolneLekturyBook(book)))
          }


          case Left(error) => {
            println(s"Failed to parse JSON: $error")
            None
          }

        }

      case Left(error) => {
        println(s"Failed to fetch data from API: $error")
        None
      }
    }
  }

  def fetchWikipediaArticle(title: String): String = {
    val url = s"https://pl.wikipedia.org/w/api.php?action=query&exlimit=1&explaintext=1&exsectionformat=plain&prop=extracts&titles=$title&format=json"
    implicit val formats: DefaultFormats = DefaultFormats
    val jsonString = Using(Source.fromURL(url)) { source =>
      source.mkString
    }.getOrElse("")

    val json = parse(jsonString)
    val extract = (json \ "query" \ "pages").extract[Map[String, JValue]].values.headOption.flatMap { page =>
      (page \ "extract").extractOpt[String]
    }

    extract.getOrElse("")
  }

  def fetchGutenbergBook(id: Int): String = {
    val url = s"https://www.gutenberg.org/cache/epub/$id/pg$id.txt"
    removeGutenbergLicense(fetchFromURL(url))
  }

  def fetchWolneLekturyBook(title: String): String = {
    val url = s"https://wolnelektury.pl/media/book/txt/$title.txt"
    removeWolneLekturyFooter(fetchFromURL(url))
  }

  private def removeWolneLekturyFooter(text: String): String = {
    val footerStart = text.indexOf("-----")
    if (footerStart != -1) text.substring(0, footerStart).trim else text
  }
  private def removeGutenbergLicense(text: String): String = {
    val start = text.indexOf("*** START OF THE PROJECT GUTENBERG EBOOK")
    val end = text.lastIndexOf("*** END OF THE PROJECT GUTENBERG EBOOK")
    if (start != -1 && end != -1) text.slice(start, end).trim else text
  }

  def extractPlainText(html: String): String = {
    val polishHtml=StringEscapeUtils.unescapeJava(html)
    polishHtml
//    val document = Jsoup.parse(polishHtml)
//    document.text()
  }

  def getWikipediaPlainText(title: String): String = {
    val fetchedText = extractPlainText(fetchWikipediaArticle(title))
    val cleanedText = fetchedText.replaceAll("[^a-zA-Z0-9ąćęłńóśźżĄĆĘŁŃÓŚŹŻ\\s]", "")
    cleanedText
  }

  def main(args: Array[String]): Unit = {
    val fetchedText = extractPlainText(fetchWikipediaArticle("pizza"))
    val cleanedText = fetchedText.replaceAll("[^a-zA-Z0-9ąćęłńóśźżĄĆĘŁŃÓŚŹŻ\\s]", "")


    println(cleanedText)

//    fetchWolneLekturyBooks()


  }


}
