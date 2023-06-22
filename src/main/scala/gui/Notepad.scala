package gui

import scala.util.Using
import scalafx.Includes._
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Alert, Button, Label, Slider, TextArea}
import scalafx.scene.input.{KeyCode, KeyEvent}
import scalafx.scene.layout.{HBox, VBox}
import model.{Model, ModelPredictor, ModelTrainer}
import scalafx.stage.FileChooser

import java.io.PrintWriter
import scala.io.Source

object Notepad extends JFXApp3 {
  private var escPressed = false
  private var autoTextSaved = false
  private var lastInsertedText = ""
  private var index = 0
  private var listOfWords = List[String]()
  private val model = new Model(ModelTrainer, ModelPredictor)
  private var textArea: TextArea = _
  private var precision = 500

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title = "Notepad"
      width = 800
      height = 600
      textArea = new TextArea()
      textArea.prefWidth = 800
      textArea.prefHeight = 500


      textArea.wrapText = true
      textArea.onKeyPressed = handleKeyPressed(_)

      private def handleKeyPressed(event: KeyEvent): Unit = {
        event.code match {
          case KeyCode.Escape =>
            if (escPressed && !autoTextSaved) {
              autoTextSaved = true
            } else {
              escPressed = true
              insertAutoText()
            }

          case _ if event.code == KeyCode.F11 || event.code == KeyCode.F12 =>
            if (listOfWords.nonEmpty && escPressed) {
              index = if (event.code == KeyCode.F11) (index + 1) % listOfWords.length else math.floorMod(index - 1, listOfWords.length)
              deleteAutoText()
              textArea.insertText(textArea.getCaretPosition, listOfWords(index))
              lastInsertedText = listOfWords(index)
            }

          case _ =>
            if (escPressed && !autoTextSaved && !(event.code == KeyCode.F11) && !(event.code == KeyCode.F12)) {
              deleteAutoText()
            }
            escPressed = false
            autoTextSaved = false
        }
      }


      val saveButton = new Button("Save")
      saveButton.onAction = _ => {
        val content = textArea.text()
        saveToFile(content, "saved.txt")
        showAlert("Zapisano", "Zapisano do pliku", "Zapisano do pliku saved.txt")

      }
      val openButton = new Button("Open")
      openButton.onAction = _ => {
        val fileChooser = new FileChooser()
        fileChooser.title = "Open File"
        val selectedFile = fileChooser.showOpenDialog(stage)
        if (selectedFile != null) {
          Using(Source.fromFile(selectedFile)) { source =>
            textArea.text = source.mkString
          }.getOrElse {
            showAlert("Błąd", "Błąd odczytu pliku", "Nie udało się odczytać pliku")
          }
        }
      }

      val vbox = new VBox(10)
      val hbox = new HBox(10)
      hbox.children = Seq(createSlider(), saveButton, openButton)
      vbox.children = Seq(textArea, hbox)
      scene = new Scene(vbox)

    }

    stage.show()

  }

  private def createSlider() = {
    val slider = new Slider()
    slider.min = 1
    slider.max = 1000
    slider.value = precision
    val sliderLabel = new Label(s"Dokładność: $precision")
    slider.valueProperty().addListener { (_, _, newValue) =>
      sliderLabel.text = s"Dokładność: ${newValue.intValue()}"
      precision = newValue.intValue()
    }

    val sliderVbox = new VBox(10)
    sliderVbox.children = Seq(slider, sliderLabel)
    sliderVbox
  }

  private def generateAutoText(lastWord: String, precision: Int): String = {
    index = 0
    if (lastWord.isEmpty) return ""
    listOfWords = model.predict(lastWord.toLowerCase().trim, "index", precision)
    listOfWords = if (listOfWords.nonEmpty) listOfWords.map(word => {
      " " + word
    }) else listOfWords
    val prediction = if (listOfWords.nonEmpty) listOfWords(index) else ""
    prediction
  }

  private def insertAutoText(): Unit = {
    val lastWord = textArea.text.value.trim.split("\\s+").last
    lastInsertedText = generateAutoText(lastWord, precision)
    textArea.insertText(textArea.getCaretPosition, lastInsertedText)
  }

  private def deleteAutoText(): Unit = {
    textArea.deleteText(textArea.getCaretPosition - lastInsertedText.length, textArea.getCaretPosition)
  }


  private def saveToFile(content: String, filename: String): Unit = {
    val writer = new PrintWriter(filename)
    writer.write(content)
    writer.close()
    println(s"Zapisano: $content")
  }

  private def showAlert(alertTitle: String, header: String, content: String): Unit = {
    new Alert(AlertType.Information) {
      title = alertTitle
      headerText = header
      contentText = content
      showAndWait()
    }

  }
}