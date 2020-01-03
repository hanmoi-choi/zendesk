package zendesk.interpreter

import cats.Monad
import cats.data.EitherT
import zendesk.dsl.Console._
import zendesk.dsl.UserInputParser._
import zendesk.dsl.{Console, Repository, UserInputParser}
import zendesk.model
import zendesk.model.{Database, QueryParams, SearchResult, Searchable}
import zendesk.service.{QueryParameterGenerator, SearchResultFormatter}
import zendesk.service.parser.SearchObjectCommand.{SearchOrganizations, SearchTickets, SearchUsers}
import zendesk.service.parser.{Parser, SearchObjectCommand}
import zendesk.util.MessageFactory

case class SearchAppModules[F[_]: Monad: Repository: Console: UserInputParser]()(
  implicit searchObjectCommandParser: Parser[SearchObjectCommand],
  queryParamsGenerator: QueryParameterGenerator,
  searchDatabase: Database,
  searchResultFormatter: SearchResultFormatter
) {

  def processSelectSearchObject(): F[Either[model.AppError, SearchObjectCommand]] = {
    val parserResult =
      for {
        _         <- EitherT(out(MessageFactory.searchObjectsOptionMessage))
        userInput <- EitherT(in())
        result    <- EitherT(parseSearchObject(userInput))
        key = result match {
          case SearchUsers => Searchable.Users
          case SearchTickets => Searchable.Tickets
          case SearchOrganizations => Searchable.Organizations

        }
        _ <- EitherT(out(searchDatabase.listOfSearchableFields(key)))
      } yield result

    parserResult.value
  }

  def processCreateQueryParams(searchObjectCommand: SearchObjectCommand): F[Either[model.AppError, QueryParams]] = {
    val parserResult =
      for {
        _                <- EitherT(out(MessageFactory.enterSearchTerm))
        searchTermInput  <- EitherT(in())
        _                <- EitherT(out(MessageFactory.enterSearchValue))
        searchValueInput <- EitherT(in())
        result           <- EitherT(parseSearchQuery(searchObjectCommand, searchTermInput, searchValueInput))
      } yield result

    parserResult.value
  }

  def searchData(queryParams: QueryParams): F[Either[model.AppError, Unit]] = {
    val parserResult =
      for {
        result <- EitherT(Repository.query(queryParams))
        _      <- EitherT(out(searchResultFormatter.format(result)))
      } yield ()

    parserResult.value
  }
}
