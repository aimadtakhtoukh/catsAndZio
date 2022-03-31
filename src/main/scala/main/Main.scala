package main

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

object Main extends App {
  def effect(int : String) : Future[Int] = {
    Future { Thread.sleep(1500); int.toInt }
  }

  def sideEffect(int: Int): Unit = {
    println(int)
  }

  Await.ready(
    effect("5")
      .map(sideEffect)
      .recover(_.printStackTrace()),
    Duration.Inf
  )

  Await.ready(
    effect("cinq")
      .map(sideEffect)
      .recover(_.printStackTrace()),
    Duration.Inf
  )
}
