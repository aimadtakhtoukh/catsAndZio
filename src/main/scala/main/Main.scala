package main

object Main extends App {
  def effect(int : String) : Int = {
    int.toInt
  }

  def sideEffect(int: Int): Unit = {
    println(int)
  }

  try {
    val eff = effect("cinq")
    sideEffect(eff)
  } catch {
    case e: Throwable => e.printStackTrace()
  }
}
