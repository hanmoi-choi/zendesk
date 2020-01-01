package zendesk.service

import zendesk.model
import zendesk.model.{AppError, QueryParams, Searchable}
import zendesk.service.parser.SearchObjectCommand._
import zendesk.service.parser._

case class QueryParameterGenerator(
                                    usersTermParser: Parser[TermsToSearchUsers],
                                    ticketsTermParser: Parser[TermsToSearchTickets],
                                    organizationsTermParser: Parser[TermsToSearchOrganizations],
                                  ) {

  def generate(searchObjectCommand: SearchObjectCommand,
               searchTermInput: String,
               searchValueInput: String): Either[AppError, QueryParams] = {
    searchObjectCommand match {
      case SearchUsers => handleValueForUsers(searchTermInput, searchValueInput)
      case SearchTickets => handleValueForTickets(searchTermInput, searchValueInput)
      case SearchOrganizations => handleValueForOrganizations(searchTermInput, searchValueInput)
    }
  }

  private def handleValueForUsers(searchTermInput: String, searchValueInput: String): Either[AppError, QueryParams] =
    for {
      userTerm <- usersTermParser.doParse(searchTermInput)
      searchValue <- userTerm.asSearchValue(searchValueInput)
    } yield model.QueryParams(
      searchKey = Searchable.Users,
      searchTerm = searchValue.getClass.getSimpleName,
      searchValue = searchValue
    )

  private def handleValueForTickets(searchTermInput: String, searchValueInput: String): scala.Either[AppError, QueryParams] =
    for {
      ticketTerm <- ticketsTermParser.doParse(searchTermInput)
      searchValue <- ticketTerm.asSearchValue(searchValueInput)
    } yield model.QueryParams(
      searchKey = Searchable.Tickets,
      searchTerm = searchValue.getClass.getSimpleName,
      searchValue = searchValue
    )

  private def handleValueForOrganizations(searchTermInput: String, searchValueInput: String): scala.Either[AppError, QueryParams] =
    for {
      organizationTerm <- organizationsTermParser.doParse(searchTermInput)
      searchValue <- organizationTerm.asSearchValue(searchValueInput)
    } yield model.QueryParams(
      searchKey = Searchable.Organizations,
      searchTerm = searchValue.getClass.getSimpleName,
      searchValue = searchValue
    )

}
