package zendesk.dsl

import cats.{Id, Monad}
import cats.effect.IO
import zendesk.model.AppError
import zendesk.util.parser.{Parser, SearchObjectCommand, SearchOptionCommand, SearchOrganizationsTerm, SearchTicketsTerm, SearchUsersTerm}

trait UserInputParser[F[_]] {
  def parseSearchOption(value: String)(implicit P: Parser[SearchOptionCommand]): F[Either[AppError, SearchOptionCommand]]
//  def parseSearchObject(value: String): F[Either[AppError, SearchObjectCommand]]
//  def parseSearchUsersTerm(value: String): F[Either[AppError, SearchUsersTerm]]
//  def parseSearchTicketsTerm(value: String): F[Either[AppError, SearchTicketsTerm]]
//  def parseSearchOrganizationsTerm(value: String): F[Either[AppError, SearchOrganizationsTerm]]
}

object UserInputParser {
  def apply[F[_]](implicit C: UserInputParser[F]): UserInputParser[F] = C


  def parseSearchOption[F[_]: UserInputParser](value: String)
                       (implicit P: Parser[SearchOptionCommand]): F[Either[AppError, SearchOptionCommand]] = {
    UserInputParser[F].parseSearchOption(value)
  }
}

