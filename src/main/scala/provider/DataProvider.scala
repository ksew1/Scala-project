package provider

import utils.Bar
import model.Data

import scala.io.Source
import scala.util.{Failure, Success, Try, Using}
import java.io._

object DataProvider {

  private def tryFetchingFromFile(filename: String): Try[List[String]] = {
    Using(Source.fromFile(filename)) { source =>
      source.getLines.toList
    }
  }

  private def fetchFromFile(filename: String): Option[List[String]] = tryFetchingFromFile(filename) match {
    case Success(lines) => Some(lines)
    case Failure(exception) =>
      println(s"Something went wrong: ${exception.getMessage}")
      None
  }

  private def validateArticles(articles: List[String]): List[String] = {
    articles.filter(Data.fetchWikipediaArticle(_).nonEmpty)
  }

  def saveToFile(line: List[String], fileName: String): Unit = {
    val file = new File(fileName)
    val pw = new PrintWriter(file)
    Using(pw) { writer =>
      line.foreach(writer.println)
    }
  }

  def findWikiArticles(): List[String] = {

    println("Fetching from file...")
    val articles = fetchFromFile("wiki_articles.txt").get

    println("Validating articles...")
    val validatedArticles = validateArticles(articles)

    println("Fetching articles...")
    fetch(validatedArticles, Data.getWikipediaPlainText)
  }

  def findWolneLekuryBooks(): List[String] = {
    println("Fetching books titles...")

    val books = Data.fetchWolneLekturyBooks().get

    println("Fetching books...")
    fetch(books, Data.fetchWolneLekturyBook)
  }

  private def fetch(data: List[String], fetchFunction: String => String): List[String] = {
    data.zipWithIndex.map { case (text, id) =>
      Bar.printProgressBar(id + 1, data.length)
      fetchFunction(text)
    }
  }




  def main(args: Array[String]): Unit = {
    /*
    val filename = "wiki_articles.txt"
    fetchFromFile(filename) match {
      case Some(lines) =>
        val validatedArticles = validateArticles(lines)
        saveToFile(validatedArticles, filename)
      case None => println("Failed to fetch data from file")
    }
    */
    findWikiArticles()
    findWolneLekuryBooks()
  }

}
