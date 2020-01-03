package zendesk.service.parser

import cats.syntax.either._
import fastparse._
import zendesk.model.{AppError, ParseFailure}
import zendesk.service.parser.TermsToSearchTickets._

object TermsToSearchTicketsParser extends Parser[TermsToSearchTickets] {
  private def parseId[_: P] = P(IgnoreCase("id")).map(_ => TicketId)

  private def parseUrl[_: P] = P(IgnoreCase("url")).map(_ => Url)

  private def parseExternalId[_: P] = P(IgnoreCase("externalId")).map(_ => ExternalId)

  private def parseType[_: P] = P(IgnoreCase("type")).map(_ => Type)

  private def parseSubject[_: P] = P(IgnoreCase("subject")).map(_ => Subject)

  private def parseDescription[_: P] = P(IgnoreCase("description")).map(_ => Description)

  private def parsePriority[_: P] = P(IgnoreCase("priority")).map(_ => Priority)

  private def parseStatus[_: P] = P(IgnoreCase("status")).map(_ => Status)

  private def parseSubmitterId[_: P] = P(IgnoreCase("submitterId")).map(_ => SubmitterId)

  private def parseAssigneeId[_: P] = P(IgnoreCase("assigneeId")).map(_ => AssigneeId)

  private def parseOrganizationId[_: P] = P(IgnoreCase("organizationId")).map(_ => OrganizationId)

  private def parseHasIncident[_: P] = P(IgnoreCase("hasIncidents")).map(_ => HasIncidents)

  private def parseDueAt[_: P] = P(IgnoreCase("dueAt")).map(_ => DueAt)

  private def parseTags[_: P] = P(IgnoreCase("tags")).map(_ => Tags)

  private def parseVia[_: P] = P(IgnoreCase("via")).map(_ => Via)

  def parseTerm[_: P]: P[TermsToSearchTickets] = P(
    parseId
      | parseUrl
      | parseExternalId
      | parseType
      | parseSubject
      | parseDescription
      | parsePriority
      | parseStatus
      | parseSubmitterId
      | parseAssigneeId
      | parseExternalId
      | parseOrganizationId
      | parseHasIncident
      | parseDueAt
      | parseTags
      | parseVia
  )

  def doParse(command: String): Either[AppError, TermsToSearchTickets] = {
    parse(command, parseTerm(_)) match {
      case Parsed.Failure(_, _, _) => ParseFailure(s"Cannot parse $command as SearchTicketsTerm").asLeft
      case Parsed.Success(v, _) => v.asRight
    }
  }
}
