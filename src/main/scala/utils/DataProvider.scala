package utils

import provider.{WikiProvider, BookProvider}

import java.io._
import scala.io.Source
import scala.util.{Failure, Success, Try, Using}

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


    println("Fetching articles...")
    val fetchArticles = fetch(articles, WikiProvider.fetchWikipediaArticle)

    println("Validating articles...")
    fetchArticles.filter(_.nonEmpty)
  }

  def findWolneLekuryBooks(): List[String] = {
    println("Fetching books titles...")

    val books = BookProvider.fetchWolneLekturyBooks().get

    println("Fetching books...")
    fetch(books, BookProvider.fetchWolneLekturyBook)
  }

  private def fetch(data: List[String], fetchFunction: String => String): List[String] = {
    data.zipWithIndex.map { case (text, id) =>
      LoadBar.printProgressBar(id + 1, data.length)
      fetchFunction(text)
    }
  }


  def main(args: Array[String]): Unit = {
    /*

    val filename = "wiki_articles.txt"
    fetchFromFile(filename) match {
      case Some(lines) =>
        val validatedArticles = lines.filter(WikiProvider.fetchWikipediaArticle(_).nonEmpty)
        saveToFile(validatedArticles, filename)
      case None => println("Failed to fetch data from file")
    }

      */

  }

}
