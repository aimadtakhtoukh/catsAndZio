package main

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

sealed trait EffectError
case object NotANumber extends EffectError
case class Unexpected(t: Throwable) extends EffectError

object Main extends App {
  def handleNumberFormatException: Throwable => EffectError = {
    case _: NumberFormatException => NotANumber
    case e => Unexpected(e)
  }

  def effect(int : String) : Future[Either[EffectError, Int]] = {
    Future { Thread.sleep(1500); int.toInt }
      .map(Right(_))
      .recover{ case t: Throwable => Left(handleNumberFormatException(t)) }
  }

  def sideEffect(int: Int): Unit = {
    println(int)
  }

  def showError: EffectError => Unit = {
    case NotANumber => System.err.println("Not a number")
    case Unexpected(t) => t.printStackTrace()
  }

  List("5", "cinq")
    .map(stringlyTypedInt =>
      effect(stringlyTypedInt)
        .map {
          case Right(value) => sideEffect(value)
          case Left(error) => showError(error)
        }
        .recover(_.printStackTrace())
    )
    .foreach(Await.ready(_, Duration.Inf))

}
