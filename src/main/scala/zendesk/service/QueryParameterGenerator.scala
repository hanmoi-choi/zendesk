package zendesk.service

import zendesk.model.AppError
import zendesk.model.value.{Active, Alias, Email, ExternalId, Id, Locale, Name, OrganizationId, Phone, QueryParams, Role, Shared, Signature, Suspended, Tag, Timezone, Url, Verified, ZenDateTime}
import zendesk.service.parser.{Parser, SearchObjectCommand, SearchOrganizationsTerm, SearchTicketsTerm, SearchUsersTerm}
import zendesk.service.parser.SearchObjectCommand._

case class QueryParameterGenerator(
                                  usersTermParser: Parser[SearchUsersTerm],
                                  ticketsTermParser: Parser[SearchTicketsTerm],
                                  organizationsTermParser: Parser[SearchOrganizationsTerm],
                                  ) {

  def generate(searchObjectCommand: SearchObjectCommand,
               searchTerm: String,
               searchValue: String): Either[AppError, QueryParams] = {
    searchObjectCommand match {
      case SearchUsers => handleValueForUsers(searchTerm, searchValue)
      case SearchTickets => handleValueForTickets(searchTerm, searchValue)
      case SearchOrganizations => handleValueForOrganizations(searchTerm, searchValue)
    }
  }

  private def handleValueForUsers(searchTerm: String, searchValue: String): Either[AppError, QueryParams] = {
    usersTermParser.doParse(searchTerm).flatMap { usersTerm =>
      parseUserValue(usersTerm, searchValue)
    }

    ???
  }
//  id: Id,
//  url: Url,
//  externalId: ExternalId,
//  name: Name,
//  alias: Option[Alias],
//  createdAt: ZenDateTime,
//  active: Active,
//  verified: Option[Verified],
//  shared: Shared,
//  locale: Option[Locale],
//  timezone: Option[Timezone],
//  lastLoginAt: ZenDateTime,
//  email: Option[Email],
//  phone: Phone,
//  signature: Signature,
//  organizationId: Option[OrganizationId],
//  tags: List[Tag],
//  suspended: Suspended,
//  role: Role
  private def parseUserValue(searchUsersTerm: SearchUsersTerm, searchValue: String) = {
    ???
  }


  private def handleValueForTickets(searchTerm: String, searchValue: String): scala.Either[AppError, QueryParams] = ???
  private def handleValueForOrganizations(searchTerm: String, searchValue: String): scala.Either[AppError, QueryParams] = ???

}
