package model

import Traits.{Predictor, Trainer}


class Model(trainer: Trainer, predictor: Predictor ) {
  def train(text: String, indexPath: String): Unit = {
    trainer.train(text: String, indexPath: String)
  }

  def predict(queryText: String, indexPath: String): List[String] = {
    predictor.predict(queryText: String, indexPath: String)
  }
}

