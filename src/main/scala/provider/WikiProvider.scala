package provider

import Traits.Provider
import org.json4s.{DefaultFormats, JValue}
import org.json4s.native.JsonMethods.parse

object WikiProvider extends Provider {

  def fetchWikipediaArticle(title: String): String = {
    implicit val formats: DefaultFormats = DefaultFormats

    val url = s"https://pl.wikipedia.org/w/api.php?action=query&exlimit=1&explaintext=1&exsectionformat=plain&prop=extracts&titles=$title&format=json"

    val jsonString = fetchFromURL(url)
    val json = parse(jsonString)
    val extract = (json \ "query" \ "pages").extract[Map[String, JValue]].values.headOption.flatMap { page =>
      (page \ "extract").extractOpt[String]
    }

    cleanText(extract.getOrElse(""))
  }

}
