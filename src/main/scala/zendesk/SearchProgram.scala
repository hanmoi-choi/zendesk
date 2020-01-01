package zendesk

import cats.Monad
import cats.data.EitherT
import zendesk.dsl.Console._
import zendesk.dsl.UserInputParser._
import zendesk.dsl.{Console, UserInputParser}
import zendesk.service.parser.{ApplicationOptionCommand, Parser, SearchObjectCommand}
import zendesk.util.MessageFactory

case class SearchProgram[F[_]: Monad: Console: UserInputParser]()(
  implicit applicationOptionCommandParser: Parser[ApplicationOptionCommand],
  searchObjectCommandParser: Parser[SearchObjectCommand]
) {

  def processSelectApplicationOptions(): F[Either[model.AppError, ApplicationOptionCommand]] = {
    val parserResult: EitherT[F, model.AppError, ApplicationOptionCommand] =
      for {
        _         <- EitherT(out(MessageFactory.appOptionsMessage))
        userInput <- EitherT(in())
        result    <- EitherT(parseSearchOption(userInput))
      } yield result

    parserResult.value
  }

  def processSelectSearchObject(): F[Either[model.AppError, SearchObjectCommand]] = {
    val parserResult: EitherT[F, model.AppError, SearchObjectCommand] =
      for {
        _         <- EitherT(out(MessageFactory.searchObjectsOptionMessage))
        userInput <- EitherT(in())
        result    <- EitherT(parseSearchObject(userInput))
      } yield result

    parserResult.value
  }
}
