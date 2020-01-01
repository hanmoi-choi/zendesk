package zendesk.interpreter

import cats.effect.IO
import cats.syntax.either._
import cats.effect.IO
import zendesk.dsl.{Console, UserInputParser}
import zendesk.model.{AppError, QueryParams}
import zendesk.service.QueryParameterGenerator
import zendesk.service.parser.{ApplicationOptionCommand, Parser, SearchObjectCommand}

import scala.io.StdIn

object IOInterpreter {

  implicit object IoEitherUserInputParser extends UserInputParser[IO] {
    override def parseSearchOption(value: String)(
      implicit P: Parser[ApplicationOptionCommand]): IO[Either[AppError, ApplicationOptionCommand]] = IO {
      P.doParse(value)
    }

    override def parseSearchObject(value: String)(
      implicit P: Parser[SearchObjectCommand]): IO[Either[AppError, SearchObjectCommand]] = IO {
      P.doParse(value)
    }

    override def parseSearchQuery(searchObjectCommand: SearchObjectCommand, termToSearch: String, searchValue: String)(
      implicit G: QueryParameterGenerator): IO[Either[AppError, QueryParams]] = IO {
      G.generate(searchObjectCommand, termToSearch, searchValue)
    }
  }

  implicit object IoEitherConsole extends Console[IO] {
    override def out(value: String): IO[Either[AppError, Unit]] = IO {
      println(value).asRight
    }

    override def in(): IO[Either[AppError, String]] = IO {
      StdIn.readLine("> ").asRight
    }
  }
}
