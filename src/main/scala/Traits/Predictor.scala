package Traits

trait Predictor {
  def predict(queryText: String, indexPath: String): List[String]
}
