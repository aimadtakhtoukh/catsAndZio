package main

import cats.data.EitherT

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import cats.syntax.all._

sealed trait EffectError
case object NotANumber extends EffectError
case class Unexpected(t: Throwable) extends EffectError

object Main extends App {
  def handleNumberFormatException: Throwable => EffectError = {
    case _: NumberFormatException => NotANumber
    case e => Unexpected(e)
  }

  def effect(int : String) : EitherT[Future, EffectError, Int] = {
    EitherT(
      Future { Thread.sleep(1500); int.toInt }
      .map(_.asRight)
      .recover{ case t: Throwable => handleNumberFormatException(t).asLeft }
    )
  }

  def sideEffect(int: Int): Unit = {
    println(int)
  }

  def showError: EffectError => Unit = {
    case NotANumber => System.err.println("Not a number")
    case Unexpected(t) => t.printStackTrace()
  }

  Await.ready(
    List("5", "cinq")
      .traverse(effect)
      .bimap(
        left => showError(left),
        right => right.foreach(sideEffect)
      )
      .value,
    Duration.Inf
  )

}
