import java.nio.file.Paths
import org.apache.lucene.analysis.shingle.ShingleAnalyzerWrapper
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.{Document, Field, TextField}
import org.apache.lucene.index.{DirectoryReader, IndexWriter, IndexWriterConfig}
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.store.FSDirectory

import scala.collection.mutable


object Model {

  def train(text: String, indexPath: String): Unit = {
    val dir = FSDirectory.open(Paths.get(indexPath))
    val analyzer = new ShingleAnalyzerWrapper(new StandardAnalyzer(), 2)
    val iwc = new IndexWriterConfig(analyzer)
    val writer = new IndexWriter(dir, iwc)

    val doc = new Document()
    doc.add(new TextField("content", text, Field.Store.YES))

    writer.addDocument(doc)
    writer.close()
  }

  def predict(queryText: String, indexPath: String): List[String] = {
    val map = mutable.Map()[String, Int]
    val reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)))
    val searcher = new IndexSearcher(reader)
    val parser = new QueryParser("content", new ShingleAnalyzerWrapper(new StandardAnalyzer(), 2))
    val query = parser.parse(queryText)
    val hits = searcher.search(query, 5).scoreDocs

    println(s"Number of hits: ${hits.length}")

    hits.foreach { hit =>
      val storedFields = searcher.storedFields()
      val hitDoc = storedFields.document(hit.doc)
      val content = hitDoc.get("content")
      val words = content.split(" ")

      if (words.length > 2) {
        val queryIndex = words.indexOf(queryText)


        if (queryIndex != -1 && queryIndex + 1 < words.length) {
          val suggestedWord = words(queryIndex + 1)
          if (!suggestedWord.equals("START") && !suggestedWord.equals("END")) {

            map(suggestedWord) = map.getOrElse(suggestedWord, 0) + 1
          }
        }
      }
    }
    println(map)
    map.toList
      .sortBy(_._2)
      .reverse
      .map(_._1)
  }

}

