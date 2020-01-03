package zendesk.interpreter

import cats.effect.IO
import cats.syntax.either._
import zendesk.dsl.Repository.queryImpl
import zendesk.dsl.{Console, Repository, UserInputParser}
import zendesk.model._
import zendesk.service.QueryParameterGenerator
import zendesk.service.parser.{Parser, SearchObjectCommand}

import scala.io.StdIn

object IOInterpreter {
  implicit object IOUserInputParser extends UserInputParser[IO] {

    override def parseSearchObject(value: String)(
      implicit P: Parser[SearchObjectCommand]): IO[Either[AppError, SearchObjectCommand]] = IO {
      P.doParse(value)
    }

    override def parseSearchQuery(searchObjectCommand: SearchObjectCommand, termToSearch: String, searchValue: String)(
      implicit G: QueryParameterGenerator): IO[Either[AppError, QueryParams]] = IO {
      G.generate(searchObjectCommand, termToSearch, searchValue)
    }

    override def stopIfUserEnteredQuit(userInput: String): IO[Either[AppError, String]] = IO {
      if (userInput.toLowerCase == UserInputParser.COMMAND_TO_QUIT_APP)
        ExitAppByUserRequest.asLeft
      else
        userInput.asRight
    }
  }

  implicit object IOConsole extends Console[IO] {
    override def out(value: String): IO[Either[AppError, Unit]] = IO {
      println(value).asRight
    }

    override def in(): IO[Either[AppError, String]] = IO {
      StdIn.readLine("> ").asRight
    }
  }

  implicit object IORepository extends Repository[IO] {
    override def query(params: QueryParams)(implicit DB: Database): IO[Either[AppError, Vector[SearchResult]]] =
      IO {
        queryImpl(params)
      }
  }
}
