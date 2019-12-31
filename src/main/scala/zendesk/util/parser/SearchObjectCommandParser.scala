package zendesk.util.parser

import fastparse._
import zendesk.model.{AppError, ParseFailure}
import cats.syntax.either._
import zendesk.util.parser.SearchObjectCommand.{Quit, SearchOrganizations, SearchTickets, SearchUsers}


object SearchObjectCommandParser {
  def parseSearchUsers[_: P]: P[SearchUsers.type] = P("1").map(_ => SearchUsers)
  def parseSearchTickets[_: P]: P[SearchTickets.type] = P("2").map(_ => SearchTickets)
  def parseSearchOrganizations[_: P]: P[SearchOrganizations.type] = P("3").map(_ => SearchOrganizations)
  def parseQuit[_: P]: P[Quit.type] = P(IgnoreCase("quit")).map(_ => Quit)
  def parseCommand[_: P]: P[SearchObjectCommand] =
    P(parseSearchUsers | parseSearchTickets | parseSearchOrganizations | parseQuit)


  def doParse(command: String): Either[AppError, SearchObjectCommand] = {
    parse(command, parseCommand(_)) match {
      case Parsed.Failure(_, _, _) => ParseFailure(s"Cannot parse $command as SearchTermCommand").asLeft
      case Parsed.Success(v, _) => v.asRight
    }
  }
}
