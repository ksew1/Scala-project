package Traits

trait Predictor {
  def predict(queryText: String, indexPath: String,numberOfHits:Int): List[String]
}
