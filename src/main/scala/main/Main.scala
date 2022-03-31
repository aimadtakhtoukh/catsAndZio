package main

import scala.util.Try

object Main extends App {
  def effect(int : String) : Try[Int] = {
    Try { int.toInt }
  }

  def sideEffect(int: Int): Unit = {
    println(int)
  }

  effect("5")
    .map(sideEffect)
    .recover(_.printStackTrace())

  effect("cinq")
    .map(sideEffect)
    .recover(_.printStackTrace())
}
