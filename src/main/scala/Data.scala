import scala.io.Source
import scala.util.Using
import org.jsoup.Jsoup
import org.json4s._
import org.json4s.native.JsonMethods._

object Data {
  private def fetchFromURL(url: String): String = {

    Using(Source.fromURL(url)) { source =>
      source.mkString


    }.getOrElse("")
  }

  def fetchWikipediaArticle(title: String): String = {
    val url = s"https://en.wikipedia.org/w/api.php?action=query&prop=extracts&format=json&exintro=&titles=$title"
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

  private def removeGutenbergLicense(text: String): String = {
    val start = text.indexOf("*** START OF THE PROJECT GUTENBERG EBOOK")
    val end = text.lastIndexOf("*** END OF THE PROJECT GUTENBERG EBOOK")
    if (start != -1 && end != -1) text.slice(start, end).trim else text
  }

  def extractPlainText(html: String): String = {
    val document = Jsoup.parse(html)
    document.text()
  }
  def main(args: Array[String]): Unit = {
    val fetchedText =extractPlainText(fetchWikipediaArticle("Albert_Einstein"))
    val cleanedText = fetchedText.replaceAll("[^a-zA-Z0-9\\s]", "")

    println(cleanedText)



  }


}
