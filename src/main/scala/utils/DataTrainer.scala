package utils

import model.Model


class DataTrainer(model: Model) {
  val indexPath = "./index/"

  def trainOnWikiArticles(): Unit = {
    val articles = DataProvider.findWikiArticles()
    println("\nTraining on wiki articles...")
    train(articles)
  }

  def trainOnWolneLektury(): Unit = {
    val books = DataProvider.findWolneLekuryBooks()
    println("\nTraining on wolne lektury...")
    train(books)
  }

  def trainOnAll(): Unit = {
    trainOnWikiArticles()
    trainOnWolneLektury()
  }

  def train(data: List[String]): Unit = {
    data.zipWithIndex.foreach { case (text, id) =>
      LoadBar.printProgressBar(id + 1, data.length)
      model.train(text, indexPath)
    }
    println()
  }
}


