package zendesk.util.parser

import fastparse._
import zendesk.util.parser.ApplicationOptionCommand.{ApplicationZendesk, ViewSearchableFields}
import zendesk.model.{AppError, ParseFailure}
import cats.syntax.either._
import zendesk.util.parser.SearchObjectCommand.{SearchOrganizations, SearchTickets, SearchUsers}

object ParserImplementation {
  implicit object ApplicationOptionCommandParser extends Parser[ApplicationOptionCommand]{
    private def parseSearchZendesk[_: P] = P("1").map(_ => ApplicationZendesk)
    private def parseViewSearchableFields[_: P] = P("2").map(_ => ViewSearchableFields)
    private def parseQuit[_: P] = P(IgnoreCase("quit")).map(_ => ApplicationOptionCommand.Quit)

    private def parseCommand[_: P] =
      P( parseSearchZendesk
        | parseViewSearchableFields
        | parseQuit
      )

    def doParse(command: String): Either[AppError, ApplicationOptionCommand] = {
      parse(command, parseCommand(_)) match {
        case Parsed.Failure(_, _, _) => ParseFailure(s"Cannot parse '$command' as ApplicationOptionCommand").asLeft
        case Parsed.Success(v, _) => v.asRight[AppError]
      }
    }
  }

  implicit object SearchObjectCommandParser extends Parser[SearchObjectCommand] {
    private def parseSearchUsers[_: P] = P("1").map(_ => SearchUsers)
    private def parseSearchTickets[_: P] = P("2").map(_ => SearchTickets)
    private def parseSearchOrganizations[_: P] = P("3").map(_ => SearchOrganizations)
    private def parseQuit[_: P] = P(IgnoreCase("quit")).map(_ => SearchObjectCommand.Quit)

    private def parseCommand[_: P]: P[SearchObjectCommand] =
      P(parseSearchUsers
        | parseSearchTickets
        | parseSearchOrganizations
        | parseQuit
      )

    def doParse(command: String): Either[AppError, SearchObjectCommand] = {
      parse(command, parseCommand(_)) match {
        case Parsed.Failure(_, _, _) => ParseFailure(s"Cannot parse '$command' as SearchObjectCommand").asLeft
        case Parsed.Success(v, _) => v.asRight
      }
    }
  }

}
