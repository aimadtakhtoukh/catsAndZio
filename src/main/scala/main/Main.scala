package main

import cats.data.EitherT
import cats.syntax.all._
import zio.{ExitCode, URIO, ZIO}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

sealed trait EffectError
case object NotANumber extends EffectError
case class Unexpected(t: Throwable) extends EffectError

object Main extends zio.App {

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = toIntApp.exitCode

  val toIntApp: ZIO[Any, Throwable, Either[Unit, Unit]] =
    ZIO.succeed(List("5", "cinq"))
      .flatMap(list => ZIO.effect(list.traverse(effect)))
      .flatMap(list =>
        ZIO.effect(
          list.bimap(
            left => showError(left),
            right => right.foreach(sideEffect)
          )
        )
      )
      .flatMap(eitherTFuture =>
        ZIO.fromFuture(_ => eitherTFuture.value)
      )


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

}
