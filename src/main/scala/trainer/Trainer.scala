package trainer

import provider.DataProvider
import model.Model
import utils.Bar


object Trainer {
  val indexPath = "./index/"

  def trainOnWikiArticles(): Unit = {
    val articles = DataProvider.findWikiArticles()
    println("Training on wiki articles...")
    train(articles)
  }

  def trainOnWolneLektury(): Unit = {
    val books = DataProvider.findWolneLekuryBooks()
    println("Training on wolne lektury...")
    train(books)
  }

  def train(data: List[String]): Unit = {
    data.zipWithIndex.foreach { case (text, id) =>
      Bar.printProgressBar(id + 1, data.length)
      Model.train(text, indexPath)
    }
    println()
  }
}


