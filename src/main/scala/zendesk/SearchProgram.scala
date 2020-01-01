package zendesk

import cats.Monad
import cats.data.EitherT
import zendesk.dsl.{Console, UserInputParser}
import zendesk.dsl.Console._
import zendesk.service.parser.ParserImplementation.{ApplicationOptionCommandParser, SearchObjectCommandParser}
import zendesk.dsl.UserInputParser._
import zendesk.util.MessageFactory
import zendesk.service.parser.{ApplicationOptionCommand, SearchObjectCommand}

class SearchProgram [F[_]: Monad: Console: UserInputParser] {

  def processSelectApplicationOptions(): F[Either[model.AppError, ApplicationOptionCommand]] ={
    val parserResult: EitherT[F, model.AppError, ApplicationOptionCommand] =
      for {
        _ <- EitherT(out(MessageFactory.appOptionsMessage))
        userInput <- EitherT(in())
        result <- EitherT(parseSearchOption(userInput))
      } yield result

    parserResult.value
  }

  def processSelectSearchObject(): F[Either[model.AppError, SearchObjectCommand]] ={
    val parserResult: EitherT[F, model.AppError, SearchObjectCommand] =
      for {
        _ <- EitherT(out(MessageFactory.searchObjectsOptionMessage))
        userInput <- EitherT(in())
        result <- EitherT(parseSearchObject(userInput))
      } yield result

    parserResult.value
  }
}
