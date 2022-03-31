package main

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object Main extends App {
  def effect(int : String) : Future[Int] = {
    Future { Thread.sleep(5000); int.toInt }
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
