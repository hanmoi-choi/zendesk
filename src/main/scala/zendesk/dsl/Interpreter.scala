package zendesk.dsl

import cats.effect.IO
import zendesk.model.AppError
import zendesk.util.parser.{Parser, SearchOptionCommand}

import scala.io.StdIn
import cats.syntax.either._

object Interpreter {
  implicit object IoEitherUserInputParser extends UserInputParser[IO] {
    override def parseSearchOption(value: String)
                                  (implicit P: Parser[SearchOptionCommand]
                                  ): IO[Either[AppError, SearchOptionCommand]] = {
      IO { P.doParse(value) }
    }
  }

  implicit object IoEitherConsole extends Console[IO] {
    override def out(value: String): IO[Either[AppError, Unit]] = IO {
      println(value).asRight
    }

    override def in(): IO[Either[AppError, String]] = IO {
      StdIn.readLine().asRight
    }
  }
}
