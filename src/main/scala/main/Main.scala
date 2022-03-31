package main

import scala.util.Try

object Main extends App {
  def effect(int : String) : Try[Int] = {
    Try { int.toInt }
  }

  def sideEffect(int: Int): Unit = {
    println(int)
  }

  def handleNumberFormatException: PartialFunction[Throwable, Unit] = {
    case _: NumberFormatException => System.err.println("The input isn't a number")
  }

  effect("5")
    .map(sideEffect)
    .recover(handleNumberFormatException)
    .recover(_.printStackTrace())

  effect("cinq")
    .map(sideEffect)
    .recover(handleNumberFormatException)
    .recover(_.printStackTrace())
}
