package zendesk.interpreter

import cats.Monad
import cats.data.EitherT
import zendesk.dsl.Console._
import zendesk.dsl.UserInputParser._
import zendesk.dsl.{Console, UserInputParser}
import zendesk.model
import zendesk.model.QueryParams
import zendesk.service.QueryParameterGenerator
import zendesk.service.parser.{ApplicationOptionCommand, Parser, SearchObjectCommand}
import zendesk.util.MessageFactory

case class SearchAppSubModules[F[_]: Monad: Console: UserInputParser]()(
  implicit applicationOptionCommandParser: Parser[ApplicationOptionCommand],
  searchObjectCommandParser: Parser[SearchObjectCommand],
  queryParamsGenerator: QueryParameterGenerator
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

  def processCreateQueryParams(searchObjectCommand: SearchObjectCommand): F[Either[model.AppError, QueryParams]] = {
    val parserResult: EitherT[F, model.AppError, QueryParams] =
      for {
        _                <- EitherT(out(MessageFactory.enterSearchTerm))
        searchTermInput  <- EitherT(in())
        _                <- EitherT(out(MessageFactory.enterSearchValue))
        searchValueInput <- EitherT(in())
        result           <- EitherT(parseSearchQuery(searchObjectCommand, searchTermInput, searchValueInput))
      } yield result

    parserResult.value
  }
}
