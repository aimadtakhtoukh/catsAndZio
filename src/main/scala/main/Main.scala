package main

import cats.syntax.all._
import zio.interop.catz.core._
import zio.{ExitCode, IO, UIO, URIO, ZIO}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

sealed trait EffectError
case object NotANumber extends EffectError
case class Unexpected(t: Throwable) extends EffectError

object Main extends zio.App {

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = toIntApp.exitCode

  val toIntApp: UIO[Unit] =
    (
      for {
        argList <- ZIO.succeed(List("5", "cinq"))
        effectList <- argList.traverse(effect)
        _ <- ZIO.effect(effectList.foreach(sideEffect)).mapError(Unexpected)
      } yield ()
    ).catchAll(showError)

  def handleNumberFormatException: Throwable => EffectError = {
    case _: NumberFormatException => NotANumber
    case e => Unexpected(e)
  }

  def effect(int : String) : IO[EffectError, Int] =
    ZIO.fromFuture(_ => Future {
      Thread.sleep(1500)
      int.toInt
    }).mapError { t: Throwable => handleNumberFormatException(t) }

  def sideEffect(int: Int): UIO[Unit] = ZIO.effectTotal(println(int))

  def showError: EffectError => UIO[Unit] = {
    case NotANumber => ZIO.effectTotal(System.err.println("Not a number"))
    case Unexpected(t) => ZIO.effectTotal(t.printStackTrace())
  }

}
