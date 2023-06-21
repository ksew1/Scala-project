package gui

import scalafx.Includes._
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Alert, Button, TextArea}
import scalafx.scene.input.{KeyCode, KeyEvent}
import scalafx.scene.layout.VBox
import model.{Model, ModelPredictor, ModelTrainer}

import java.io.PrintWriter

object Notepad extends JFXApp3 {
  private var tabPressed = false
  private var autoTextSaved = false
  private var lastInsertedText = ""
  private var index = 0
  private var listOfWords = List[String]()
  private val  model = new Model(ModelTrainer, ModelPredictor)

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title = "Notepad"
      width = 600
      height = 400

      val textArea = new TextArea()
      textArea.wrapText = true

      textArea.onKeyPressed = (event: KeyEvent) => {
        println(event.code)
        if (event.code == KeyCode.Escape) {
          if (tabPressed && !autoTextSaved) {
            autoTextSaved = true
            saveAutoText(textArea)
          } else {
            tabPressed = true
            insertAutoText(textArea)
          }
        } else {
          if (tabPressed && !autoTextSaved) {
            deleteAutoText(textArea)
          }
          tabPressed = false
          autoTextSaved = false
        }

        if (listOfWords.nonEmpty) {
          if (event.code == KeyCode.Q) {
            index = (index + 1) % listOfWords.length
            textArea.insertText(textArea.getCaretPosition, listOfWords(index))
          }
        }
      }


      val saveButton = new Button("Save")
      saveButton.onAction = _ => {
        val content = textArea.text()
        saveToFile(content, "saved.txt")
        showAlert("Zapisano", "Zapisano do pliku", "Zapisano do pliku saved.txt")

      }
      val vbox = new VBox(10)

      vbox.children = Seq(textArea, saveButton)
      scene = new Scene(vbox)

    }

    stage.show()

  }

  private def generateAutoText(lastWord:String): String = {
    index = 0
    listOfWords = model.predict(lastWord, "index")
    println(s"Lista słów: $listOfWords")
    val prediction = listOfWords(index)
    println(s"Prediction: $prediction")
    val out = if (prediction.nonEmpty) " "+prediction else prediction
    out
  }

  private def insertAutoText(textArea: TextArea): Unit = {
    val lastWord = textArea.text.value.trim.split("\\s+").last
    println(s"Ostatnie słowo: $lastWord")
    lastInsertedText = generateAutoText(lastWord)
    textArea.insertText(textArea.getCaretPosition, lastInsertedText)
  }

  private def deleteAutoText(textArea: TextArea): Unit = {
    textArea.deleteText(textArea.getCaretPosition - lastInsertedText.length, textArea.getCaretPosition)
  }

  private def saveAutoText(textArea: TextArea): Unit = {
    val autoText = textArea.text.value
  }


  private def saveToFile(content: String, filename: String): Unit = {
    val writer = new PrintWriter(filename)
    writer.write(content)
    writer.close()
    println(s"Zapisano: $content")
  }

  private def showAlert(alertTitle: String, header: String, content: String): Unit = {
    val alert = new Alert(AlertType.Information) {
      title = alertTitle
      headerText = header
      contentText = content
      showAndWait()
    }

  }
}