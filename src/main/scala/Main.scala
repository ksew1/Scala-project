import model.{Data, Model}
import trainer.Trainer

object Main {
  def main(args: Array[String]): Unit = {
    val indexPath = "./index/"

    println("Training")
//    Trainer.trainOnWikiArticles()
//    Trainer.trainOnWolneLektury()

    println("Predicting")
    val predictionTexts = List("pyszne piwo","polska", "kremówki", "świat", "kot",
      "dom",
      "samochód",
      "szkoła",
      "kawa",
      "rząd",
      "książka",
      "drzewo",
      "piłka",
      "muzyka",
      "komputer",
      "telewizor",
      "sport",
      "serce",
      "miłość","piwo","piwko","zazwyczaj")

    predictionTexts.foreach { pt =>
      println(s"\nPredictions for '$pt':")
      println(Model.predict(pt, indexPath))
    }
  }
}
