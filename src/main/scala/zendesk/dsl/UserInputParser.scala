package zendesk.dsl

import zendesk.model.{AppError, QueryParams}
import zendesk.service.QueryParameterGenerator
import zendesk.service.parser.{Parser, SearchObjectCommand}

trait UserInputParser[F[_]] {
  def parseSearchObject(value: String)(
    implicit P: Parser[SearchObjectCommand]): F[Either[AppError, SearchObjectCommand]]

  def parseSearchQuery(searchObjectCommand: SearchObjectCommand, termToSearch: String, searchValue: String)(
    implicit G: QueryParameterGenerator): F[Either[AppError, QueryParams]]
}

object UserInputParser {
  def apply[F[_]](implicit C: UserInputParser[F]): UserInputParser[F] = C

  def parseSearchObject[F[_]: UserInputParser](value: String)(
    implicit P: Parser[SearchObjectCommand]): F[Either[AppError, SearchObjectCommand]] =
    UserInputParser[F].parseSearchObject(value)

  def parseSearchQuery[F[_]: UserInputParser](
    searchObjectCommand: SearchObjectCommand,
    termToSearch: String,
    searchValue: String)(implicit G: QueryParameterGenerator): F[Either[AppError, QueryParams]] =
    UserInputParser[F].parseSearchQuery(searchObjectCommand, termToSearch, searchValue)
}
