import scala.io.Source
import scala.util.Using

object Data {
  private def fetchFromURL(url: String): String = {
    Using(Source.fromURL(url)) {
      _.mkString
    }.getOrElse("")
  }

  def fetchWikipediaArticle(title: String): String = {
    val url = s"https://en.wikipedia.org/w/api.php?action=query&prop=extracts&format=json&exintro=&titles=$title"
    fetchFromURL(url)
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

  def main(args: Array[String]): Unit = {
    println(fetchGutenbergBook(1342))
  }


}
