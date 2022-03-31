package main

import scala.util.Try

sealed trait EffectError
case object NotANumber extends EffectError
case class Unexpected(t: Throwable) extends EffectError

object Main extends App {
  def handleNumberFormatException: Throwable => EffectError = {
    case _: NumberFormatException => NotANumber
    case e => Unexpected(e)
  }

  def effect(int : String) : Either[EffectError, Int] = {
    Try { int.toInt }
      .toEither
      .left
      .map(handleNumberFormatException)
  }

  def sideEffect(int: Int): Int = {
    println(int); int
  }

  def showError: EffectError => Unit = {
    case NotANumber => System.err.println("Not a number")
    case Unexpected(t) => t.printStackTrace()
  }

  effect("5").map(sideEffect).left.map(showError)

  effect("cinq") match {
    case Right(value) => sideEffect(value)
    case Left(effectError) => showError(effectError)
  }
}
