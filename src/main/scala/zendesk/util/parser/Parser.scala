package zendesk.util.parser

import zendesk.model.AppError

trait Parser[T] {
  def doParse(command: String): Either[AppError, T]
}
