package model

import Traits.Trainer
import org.apache.lucene.analysis.shingle.ShingleAnalyzerWrapper
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.{Document, Field, TextField}
import org.apache.lucene.index.{IndexWriter, IndexWriterConfig}
import org.apache.lucene.store.FSDirectory

import java.nio.file.Paths

object ModelTrainer extends Trainer {
  private def createWriter(indexPath: String, analyzer: ShingleAnalyzerWrapper): IndexWriter = {
    val dir = FSDirectory.open(Paths.get(indexPath))
    val iwc = new IndexWriterConfig(analyzer)
    new IndexWriter(dir, iwc)
  }

  private def createDocument(text: String): Document = {
    val doc = new Document()
    doc.add(new TextField("content", text, Field.Store.YES))
    doc
  }

  private def addDocumentToWriter(writer: IndexWriter, doc: Document): Unit = {
    writer.addDocument(doc)
    writer.close()
  }

  def train(text: String, indexPath: String): Unit = {
    val analyzer = new ShingleAnalyzerWrapper(new StandardAnalyzer(), 2)
    val writer = createWriter(indexPath, analyzer)
    val doc = createDocument(text)
    addDocumentToWriter(writer, doc)
  }

  /*
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

   */

}
