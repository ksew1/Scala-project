package utils

object Bar {
  def printProgressBar(current: Int, total: Int): Unit = {
    val percent = (current.toDouble / total.toDouble * 100).toInt
    val progressBar = "[" + "=" * (percent / 10) + " " * ((100 - percent) / 10) + "] " + percent + "%"
    print("\r" + progressBar)
  }
}
