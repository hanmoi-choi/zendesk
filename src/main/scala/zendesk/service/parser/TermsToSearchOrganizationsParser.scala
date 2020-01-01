package zendesk.service.parser

import cats.syntax.either._
import fastparse._
import zendesk.model.{AppError, ParseFailure}
import zendesk.service.parser.TermsToSearchOrganizations._

object TermsToSearchOrganizationsParser extends Parser[TermsToSearchOrganizations] {
  private def parseId[_: P] = P(IgnoreCase("id")).map(_ => Id)

  private def parseUrl[_: P] = P(IgnoreCase("url")).map(_ => Url)

  private def parseExternalId[_: P] = P(IgnoreCase("externalId")).map(_ => ExternalId)

  private def parseName[_: P] = P(IgnoreCase("name")).map(_ => Name)

  private def parseDomainNames[_: P] = P(IgnoreCase("domainNames")).map(_ => DomainNames)

  private def parseCreatedAt[_: P] = P(IgnoreCase("createdAt")).map(_ => CreatedAt)

  private def parseDetails[_: P] = P(IgnoreCase("details")).map(_ => Details)

  private def parseSharedTickets[_: P] = P(IgnoreCase("sharedTickets")).map(_ => SharedTickets)

  private def parseTags[_: P] = P(IgnoreCase("tags")).map(_ => Tags)

  private def parseQuit[_: P] = P(IgnoreCase("quit")).map(_ => Quit)

  def parseTerm[_: P]: P[TermsToSearchOrganizations] = P(
    parseId
      | parseUrl
      | parseExternalId
      | parseName
      | parseDomainNames
      | parseCreatedAt
      | parseTags
      | parseDetails
      | parseSharedTickets
      | parseQuit
  )

  def doParse(command: String): Either[AppError, TermsToSearchOrganizations] = {
    parse(command, parseTerm(_)) match {
      case Parsed.Failure(_, _, _) => ParseFailure(s"Cannot parse $command as SearchOrganizationsTerm").asLeft
      case Parsed.Success(v, _) => v.asRight
    }
  }
}
