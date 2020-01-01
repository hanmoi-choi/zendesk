package zendesk.service.parser

import cats.syntax.either._
import fastparse._
import zendesk.model.{AppError, ParseFailure}
import zendesk.service.parser.ApplicationOptionCommand.{ApplicationZendesk, ViewSearchableFields}

case class ApplicationOptionCommandParser() extends Parser[ApplicationOptionCommand] {
  private def parseSearchZendesk[_: P] = P("1").map(_ => ApplicationZendesk)

  private def parseViewSearchableFields[_: P] = P("2").map(_ => ViewSearchableFields)

  private def parseQuit[_: P] = P(IgnoreCase("quit")).map(_ => ApplicationOptionCommand.Quit)

  private def parseCommand[_: P] =
    P(
      parseSearchZendesk
        | parseViewSearchableFields
        | parseQuit)

  def doParse(command: String): Either[AppError, ApplicationOptionCommand] = {
    parse(command, parseCommand(_)) match {
      case Parsed.Failure(_, _, _) => ParseFailure(s"Cannot parse '$command' as ApplicationOptionCommand").asLeft
      case Parsed.Success(v, _) => v.asRight[AppError]
    }
  }
}
