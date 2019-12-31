package zendesk.util.parser

import fastparse._
import zendesk.util.parser.SearchOptionCommand.{Quit, SearchZendesk, ViewSearchableFields}
import zendesk.model.{AppError, ParseFailure}
import cats.syntax.either._

object ParserImplements {
  implicit object SearchOptionCommandParser extends Parser[SearchOptionCommand]{
    private def parseSearchZendesk[_: P] = P("1").map(_ => SearchZendesk)
    private def parseViewSearchableFields[_: P] = P("2").map(_ => ViewSearchableFields)
    private def parseQuit[_: P] = P(IgnoreCase("quit")).map(_ => Quit)

    private def parseCommand[_: P] =
      P( parseSearchZendesk
        | parseViewSearchableFields
        | parseQuit
      )

    def doParse(command: String): Either[AppError, SearchOptionCommand] = {
      parse(command, parseCommand(_)) match {
        case Parsed.Failure(_, _, _) => ParseFailure(s"Cannot parse '$command' as SearchOptionCommand").asLeft
        case Parsed.Success(v, _) => v.asRight[AppError]
      }
    }
  }
}
