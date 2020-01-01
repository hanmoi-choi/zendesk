package zendesk.interpreter

import cats.syntax.either._
import cats.effect.IO
import zendesk.dsl.{Console, Repository, UserInputParser}
import zendesk.model.{AppError, DataNotfound, QueryParams, Searchable}
import zendesk.service.QueryParameterGenerator
import zendesk.service.parser.{ApplicationOptionCommand, Parser, SearchObjectCommand}
import zendesk.util.SearchDatabase

import scala.io.StdIn

object IOInterpreter {

  implicit object IOUserInputParser extends UserInputParser[IO] {
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

  implicit object IOConsole extends Console[IO] {
    override def out(value: String): IO[Either[AppError, Unit]] = IO {
      println(value).asRight
    }

    override def in(): IO[Either[AppError, String]] = IO {
      StdIn.readLine("> ").asRight
    }
  }

  implicit object IORepository extends Repository[IO] {
    override def query(params: QueryParams)(implicit DB: SearchDatabase): IO[Either[AppError, Vector[Searchable]]] =
      IO {
        DB.query(params) match {
          case Vector() =>
            val errorMessage =
              s"${params.searchKey} with Term('${params.searchTerm}') and Value('${params.searchValue}') is not found"
            DataNotfound(errorMessage).asLeft
          case v => v.asRight[AppError]
        }
      }
  }
}
