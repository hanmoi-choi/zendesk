package zendesk

import cats.effect.{ExitCode, IO, IOApp}
import cats.instances.string._
import cats.syntax.semigroup._

object Main extends IOApp {
  println("Hello " |+| "Cats!")

  override def run(args: List[String]): IO[ExitCode] = ???
}
