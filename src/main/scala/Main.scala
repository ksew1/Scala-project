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
    val titles = Array(
      "Jan_Pawel_II",
      "Robert_Lewandowski",
      "Robert_Kubica",
      "Polska",
      "Agnieszka_Radwańska",
      "Małgorzata_Gersdorf",
      "Wojciech_Szczęsny",
      "Piotr_Anderszewski",
      "Anna_Dymna",
      "Joanna_Senyszyn",
      "Katarzyna_Kozyra",
      "Paweł_Pawlikowski",
      "Jerzy_Dudek",
      "Magdalena_Abacjewska",
      "Tomasz_Starzewski",
      "Orkiestra_Smyczkowa_Iuventus",
      "Festiwal_Solidarity_of_Arts",
      "Warszawskie_Targi_Książki",
      "Festiwal_Filmowy_w_Gdyni",
      "Festiwal_Chopin_i_Jego_Europa",
      "Międzynarodowy_Festiwal_Kraków_Remuh",
      "Maria_Skłodowska-Curie",
      "Fryderyk_Chopin",
      "Jan_Kochanowski",
      "Henryk_Sienkiewicz",
      "Władysław_Reymont",
      "Lech_Wałęsa",
      "Mikołaj_Kopernik",
      "Stanisław_Lem",
      "Jerzy_Grotowski",
      "Andrzej_Wajda",
      "Wisława_Szymborska",
      "Krzysztof_Kieślowski",
      "Czesław_Miłosz",
      "Tadeusz_Różewicz",
      "Zbigniew_Herbert",
      "Ryszard_Kapuściński",
      "Aleksander_Fredro",
      "Zofia_Nalkowska",
      "Stefan_Żeromski",
      "Maria_Konopnicka",
      "Juliusz_Słowacki",
      "Bitwa pod Waterloo",
      "Upadek Cesarstwa Rzymskiego",
      "Rewolucja francuska",
      "Wyprawa Apollo 11",
      "Zamach na World Trade Center",
      "Stany Zjednoczone",
      "Wielka Brytania",
      "Rosja",
      "Chiny",
      "Indie",
      "Elon Musk",
      "Malala Yousafzai",
      "Angela Merkel",
      "Cristiano Ronaldo",
      "Greta Thunberg",
      "Donald Trump"
    )
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
