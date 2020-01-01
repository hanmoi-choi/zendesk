package zendesk.dsl

import cats.effect.IO
import zendesk.model.AppError
import zendesk.service.parser.{ApplicationOptionCommand, Parser, SearchObjectCommand}

import scala.io.StdIn
import cats.syntax.either._

object Interpreter {
  implicit object IoEitherUserInputParser extends UserInputParser[IO] {
    override def parseSearchOption(value: String)
                                  (implicit P: Parser[ApplicationOptionCommand]
                                  ): IO[Either[AppError, ApplicationOptionCommand]] = {
      IO { P.doParse(value) }
    }

    override def parseSearchObject(value: String)(implicit P: Parser[SearchObjectCommand]
    ): IO[Either[AppError, SearchObjectCommand]] = {
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
