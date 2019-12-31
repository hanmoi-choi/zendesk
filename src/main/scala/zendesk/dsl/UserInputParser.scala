package zendesk.dsl

import cats.{Id, Monad}
import cats.effect.IO
import zendesk.model.AppError
import zendesk.util.parser.{Parser, SearchObjectCommand, ApplicationOptionCommand, SearchOrganizationsTerm, SearchTicketsTerm, SearchUsersTerm}

trait UserInputParser[F[_]] {
  def parseSearchOption(value: String)(implicit P: Parser[ApplicationOptionCommand]): F[Either[AppError, ApplicationOptionCommand]]
  def parseSearchObject(value: String)(implicit P: Parser[SearchObjectCommand]): F[Either[AppError, SearchObjectCommand]]
//  def parseSearchUsersTerm(value: String): F[Either[AppError, SearchUsersTerm]]
//  def parseSearchTicketsTerm(value: String): F[Either[AppError, SearchTicketsTerm]]
//  def parseSearchOrganizationsTerm(value: String): F[Either[AppError, SearchOrganizationsTerm]]
}

object UserInputParser {
  def apply[F[_]](implicit C: UserInputParser[F]): UserInputParser[F] = C


  def parseSearchOption[F[_]: UserInputParser](value: String)
                       (implicit P: Parser[ApplicationOptionCommand]
                       ): F[Either[AppError, ApplicationOptionCommand]] = {
    UserInputParser[F].parseSearchOption(value)
  }

  def parseSearchObject[F[_]: UserInputParser](value: String)
                                              (implicit P: Parser[SearchObjectCommand]
                                              ): F[Either[AppError, SearchObjectCommand]] = {
    UserInputParser[F].parseSearchObject(value)
  }
}

