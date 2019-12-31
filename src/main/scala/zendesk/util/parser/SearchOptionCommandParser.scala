package zendesk.util.parser

import fastparse._
import zendesk.util.parser.SearchOptionCommand.{Quit, SearchZendesk, ViewSearchableFields}
import zendesk.model.{AppError, ParseFailure}
import cats.syntax.either._


object SearchOptionCommandParser {
  def parseSearchZendesk[_: P]: P[SearchZendesk.type] = P("1").map(_ => SearchZendesk)
  def parseViewSearchableFields[_: P]: P[ViewSearchableFields.type] = P("2").map(_ => ViewSearchableFields)
  def parseQuit[_: P]: P[Quit.type] = P(IgnoreCase("quit")).map(_ => Quit)
  def parseCommand[_: P]: P[SearchOptionCommand] = P(parseSearchZendesk | parseViewSearchableFields | parseQuit)


  def parseSearchOptionCommand(command: String): Either[AppError, SearchOptionCommand] = {
    parse(command, parseCommand(_)) match {
      case Parsed.Failure(_, _, _) => ParseFailure(s"Cannot parse $command as SearchOptionCommand").asLeft
      case Parsed.Success(v, _) => v.asRight
    }
  }
}
