import model.{Model, ModelPredictor, ModelTrainer}
import utils.DataTrainer


object Main {
  def main(args: Array[String]): Unit = {
    val indexPath = "./index/"
    val model = new Model(ModelTrainer, ModelPredictor)
    val dataTrainer = new DataTrainer(model)


//    println("Training")
//    dataTrainer.trainOnAll()


    println("Predicting")
    val predictionTexts = List("polska jest","polska", "kremówki", "świat", "kot",
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
      println(s"\n\nPredictions for '$pt':\n")

      val prediction =  model.predict(pt, indexPath,500).map(_.replaceAll("\\n", " "))
      println(prediction.mkString("\n"))
    }
  }
}
