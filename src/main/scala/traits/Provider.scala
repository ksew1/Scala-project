package traits

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
      .toLowerCase()
      .replaceAll("[^a-z0-9ąćęłńóśźż\\s]", "")
      .replaceAll("\\n\\s+", " ")
      .trim()
  }

}
