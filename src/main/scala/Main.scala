object Main {
  def main(args: Array[String]): Unit = {
    val trainingText = "START The quick brown fox jumps over the lazy dog END " +
      "START Lucene is a powerful text search engine library END " +
      "START The world is full of beautiful places END " +
      "START Machine learning is a fascinating field END " +
      "START Coffee is a popular morning beverage END " +
      "START Science is constantly evolving and changing END " +
      "START In the world of programming, Java is a popular language END " +
      "START Video games are a popular form of entertainment END " +
      "START Reading books is a great way to gain knowledge END " +
      "START Python is a versatile language used in multiple fields END " +
      "START Artificial intelligence is the future of technology END " +
      "START OpenAI has developed powerful AI models END " +
      "START Nature is full of wonderful creatures END " +
      "START Many people enjoy the taste of coffee END " +
      "START Learning programming can be challenging but rewarding END " +
      "START Python's simplicity makes it a great language for beginners END " +
      "START Healthy eating is essential for good health END " +
      "START Regular exercise contributes to overall well-being END " +
      "START Painting is a form of creative expression END " +
      "START Football is a popular sport worldwide END " +
      "START Traveling allows us to experience different cultures END"

    val indexPath = "./index/"

    //Model.train(trainingText, indexPath)


    val predictionTexts = List("quick", "Lucene", "world", "learning", "Coffee", "Science",
      "programming", "games", "Reading", "Python", "intelligence",
      "OpenAI", "Nature", "people", "Health", "exercise", "Painting",
      "Football", "Traveling", "Kawa")

    predictionTexts.foreach { pt =>
      println(s"\nPredictions for '$pt':")
      Model.predict(pt, indexPath)
    }
  }
}
