package Traits

import org.apache.commons.text.StringEscapeUtils

import scala.io.Source
import scala.util.Using

trait Provider {
  def fetchFromURL(url: String): String = {
    Using(Source.fromURL(url)) { source =>
      source.mkString
    }.getOrElse("")
  }


  def cleanText(html: String): String = {
    StringEscapeUtils
      .unescapeJava(html)
      .replaceAll("[^a-zA-Z0-9ąćęłńóśźżĄĆĘŁŃÓŚŹŻ\\s]", "")
      .replaceAll("\\n\\s+", " ")
      .toLowerCase()
  }

  /*
  def fetchGutenbergBook(id: Int): String = {
    val url = s"https://www.gutenberg.org/cache/epub/$id/pg$id.txt"
    removeGutenbergLicense(fetchFromURL(url))
  }

  private def removeGutenbergLicense(text: String): String = {
    val start = text.indexOf("*** START OF THE PROJECT GUTENBERG EBOOK")
    val end = text.lastIndexOf("*** END OF THE PROJECT GUTENBERG EBOOK")
    if (start != -1 && end != -1) text.slice(start, end).trim else text
  }
  */

}
