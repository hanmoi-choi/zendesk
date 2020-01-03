package zendesk.service.parser

import cats.syntax.either._
import fastparse._
import zendesk.model.{AppError, ParseFailure}
import zendesk.service.parser.SearchObjectCommand.{SearchOrganizations, SearchTickets, SearchUsers}

case class SearchObjectCommandParser() extends Parser[SearchObjectCommand] {
  private def parseSearchUsers[_: P] = P("1").map(_ => SearchUsers)

  private def parseSearchTickets[_: P] = P("2").map(_ => SearchTickets)

  private def parseSearchOrganizations[_: P] = P("3").map(_ => SearchOrganizations)

  private def parseCommand[_: P]: P[SearchObjectCommand] =
    P(
      parseSearchUsers
        | parseSearchTickets
        | parseSearchOrganizations)

  def doParse(command: String): Either[AppError, SearchObjectCommand] = {
    parse(command, parseCommand(_)) match {
      case Parsed.Failure(_, _, _) => ParseFailure(s"Cannot parse '$command' as SearchObjectCommand").asLeft
      case Parsed.Success(v, _) => v.asRight
    }
  }
}
