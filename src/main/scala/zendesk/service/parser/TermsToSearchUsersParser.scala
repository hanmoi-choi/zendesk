package zendesk.service.parser

import cats.syntax.either._
import fastparse._
import zendesk.model.{AppError, ParseFailure}
import zendesk.service.parser.TermsToSearchUsers._

object TermsToSearchUsersParser extends Parser[TermsToSearchUsers] {
  private def parseId[_: P] = P(IgnoreCase("id")).map(_ => Id)

  private def parseUrl[_: P] = P(IgnoreCase("url")).map(_ => Url)

  private def parseExternalId[_: P] = P(IgnoreCase("externalId")).map(_ => ExternalId)

  private def parseName[_: P] = P(IgnoreCase("name")).map(_ => Name)

  private def parseAlias[_: P] = P(IgnoreCase("alias")).map(_ => Alias)

  private def parseCreatedAt[_: P] = P(IgnoreCase("createdAt")).map(_ => CreatedAt)

  private def parseActive[_: P] = P(IgnoreCase("active")).map(_ => Active)

  private def parseVerified[_: P] = P(IgnoreCase("verified")).map(_ => Verified)

  private def parseShared[_: P] = P(IgnoreCase("shared")).map(_ => Shared)

  private def parseLocale[_: P] = P(IgnoreCase("locale")).map(_ => Locale)

  private def parseTimezone[_: P] = P(IgnoreCase("timezone")).map(_ => Timezone)

  private def parseLastLoginAt[_: P] = P(IgnoreCase("lastLoginAt")).map(_ => LastLoginAt)

  private def parseEmail[_: P] = P(IgnoreCase("email")).map(_ => Email)

  private def parsePhone[_: P] = P(IgnoreCase("phone")).map(_ => Phone)

  private def parseSignature[_: P] = P(IgnoreCase("signature")).map(_ => Signature)

  private def parseOrganizationId[_: P] = P(IgnoreCase("organizationId")).map(_ => OrganizationId)

  private def parseTags[_: P] = P(IgnoreCase("tags")).map(_ => Tags)

  private def parseSuspended[_: P] = P(IgnoreCase("suspended")).map(_ => Suspended)

  private def parseRole[_: P] = P(IgnoreCase("role")).map(_ => Role)

  private def parseQuit[_: P] = P(IgnoreCase("quit")).map(_ => Quit)

  def parseTerm[_: P]: P[TermsToSearchUsers] = P(
    parseId
      | parseUrl
      | parseExternalId
      | parseName
      | parseAlias
      | parseCreatedAt
      | parseActive
      | parseVerified
      | parseShared
      | parseLocale
      | parseTimezone
      | parseLastLoginAt
      | parseEmail
      | parsePhone
      | parseSignature
      | parseOrganizationId
      | parseTags
      | parseSuspended
      | parseRole
      | parseQuit
  )


  def doParse(command: String): Either[AppError, TermsToSearchUsers] = {
    parse(command, parseTerm(_)) match {
      case Parsed.Failure(_, _, _) => ParseFailure(s"Cannot parse $command as SearchUsersTerm").asLeft
      case Parsed.Success(v, _) => v.asRight
    }
  }
}
