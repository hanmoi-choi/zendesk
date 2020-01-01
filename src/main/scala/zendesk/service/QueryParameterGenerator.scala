package zendesk.service

import zendesk.model.AppError
import zendesk.model.value.QueryParams
import zendesk.model.value.SearchObject.{Ticket, User}
import zendesk.service.parser.SearchObjectCommand._
import zendesk.service.parser._

case class QueryParameterGenerator(
                                  usersTermParser: Parser[SearchUsersTerm],
                                  ticketsTermParser: Parser[SearchTicketsTerm],
                                  organizationsTermParser: Parser[SearchOrganizationsTerm],
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
    } yield QueryParams(User, searchValue.getClass.getSimpleName, searchValue)

  private def handleValueForTickets(searchTermInput: String, searchValueInput: String): scala.Either[AppError, QueryParams] =
    for {
      ticketTerm <- ticketsTermParser.doParse(searchTermInput)
      searchValue <- ticketTerm.asSearchValue(searchValueInput)
    } yield QueryParams(Ticket, searchValue.getClass.getSimpleName, searchValue)

  private def handleValueForOrganizations(searchTerm: String, searchValue: String): scala.Either[AppError, QueryParams] = ???

}
