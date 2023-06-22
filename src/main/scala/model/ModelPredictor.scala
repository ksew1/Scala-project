package model

import Traits.Predictor
import org.apache.lucene.analysis.shingle.ShingleAnalyzerWrapper
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.{IndexSearcher, ScoreDoc}
import org.apache.lucene.store.FSDirectory

import java.nio.file.Paths
import scala.collection.mutable

object ModelPredictor extends Predictor {

  private def getQueryIndex(words: Array[String], queryText: String): Option[Int] = {
    if (words.length > 2) Some(words.indexOf(queryText)) else None
  }

  private def getSuggestedWord(queryIndex: Int, words: Array[String]): Option[String] = {
    if (queryIndex != -1 && queryIndex + 1 < words.length) Some(words(queryIndex + 1)) else None
  }

  private def getWords(hit: ScoreDoc, searcher: IndexSearcher): Array[String] = {
    val storedFields = searcher.storedFields()
    val hitDoc = storedFields.document(hit.doc)
    val content = hitDoc.get("content")
    content.split(" ")
  }

  private def sortAndFilterResults(map: mutable.Map[String, Int]): List[String] = {
    map.toList
      .filterNot(_._1.contains("\n"))
      .sortBy(_._2)
      .reverse
      .map(_._1)
      .filter(_.length > 3)
      .take(10)
  }

  private def findPrediction(hit: ScoreDoc, queryText: String, searcher: IndexSearcher): Option[String] = {
    val words = getWords(hit, searcher)
    val queryIndex = getQueryIndex(words, queryText).getOrElse(-1)
    getSuggestedWord(queryIndex, words)
  }

  private def getSearchHits(queryText: String, searcher: IndexSearcher,numberOfHits:Int): Array[ScoreDoc] = {
    val parser = new QueryParser("content", new ShingleAnalyzerWrapper(new StandardAnalyzer(), 2))
    val query = parser.parse(queryText)
    searcher.search(query, numberOfHits).scoreDocs
  }

  private def processHits(queryText: String, searcher: IndexSearcher, map: mutable.Map[String, Int],numberOfHits:Int): List[String] = {
    getSearchHits(queryText, searcher,numberOfHits).foreach { hit =>
      findPrediction(hit, queryText, searcher) match {
        case Some(suggestedWord) => map(suggestedWord) = map.getOrElse(suggestedWord, 0) + 1
        case None => ()
      }
    }
    sortAndFilterResults(map)
  }

  def predict(queryText: String, indexPath: String,numberOfHits:Int): List[String] = {
    val map: mutable.Map[String, Int] = mutable.Map.empty[String, Int]
    val reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)))
    val searcher = new IndexSearcher(reader)

    processHits(queryText, searcher, map,numberOfHits:Int)
  }
  /*
  def predict(queryText: String, indexPath: String): List[String] = {
    val map: mutable.Map[String, Int] = mutable.Map.empty[String, Int]
    val reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)))
    val searcher = new IndexSearcher(reader)
    val parser = new QueryParser("content", new ShingleAnalyzerWrapper(new StandardAnalyzer(), 2))
    val query = parser.parse(queryText)

    val hits = searcher.search(query, 500).scoreDocs


    hits.foreach { hit =>
      val storedFields = searcher.storedFields()
      val hitDoc = storedFields.document(hit.doc)
      val content = hitDoc.get("content")
      val words = content.split(" ")

      val queryIndex = getQueryIndex(words, queryText).getOrElse(-1)

      getSuggestedWord(queryIndex, words) match {
        case Some(suggestedWord) => map(suggestedWord) = map.getOrElse(suggestedWord, 0) + 1
        case None => ()
      }

    }
    map.toList
      .sortBy(_._2)
      .reverse
      .map(_._1)
      .filter(_.length > 3)
      .take(10)

  }
   */

}
