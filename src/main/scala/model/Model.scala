package model

import traits.{Predictor, Trainer}


class Model(trainer: Trainer, predictor: Predictor) extends Trainer with Predictor {
  def train(text: String, indexPath: String): Unit = {
    trainer.train(text: String, indexPath: String)
  }

  def predict(queryText: String, indexPath: String, numberOfHits: Int): List[String] = {
    predictor.predict(queryText: String, indexPath: String, numberOfHits: Int)
  }
}

