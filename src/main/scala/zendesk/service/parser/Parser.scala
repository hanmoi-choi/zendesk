package zendesk.service.parser

import cats.syntax.either._
import zendesk.model.value.{EmptyStringSearchField, SearchValue}
import zendesk.model.{AppError, InvalidArgumentError}

import scala.util.{Failure, Success, Try}

trait Parser[T] {
  def doParse(command: String): Either[AppError, T]
}

object Parser {
  def trimWhiteSpace(value: String): String = value.replaceAll(" ", "")

  def emptyStringAsEmptyStringSearchField(value: String)(
    nonEmptyStringParser: String => Either[AppError, SearchValue]
  ): Either[AppError, SearchValue] = {
    value match {
      case "" => EmptyStringSearchField.asRight
      case _ => nonEmptyStringParser(value)
    }
  }

  def prohibitEmptyString(value: String)(
    nonEmptyStringParser: String => Either[AppError, SearchValue]
  ): Either[AppError, SearchValue] = {
    value match {
      case "" => InvalidArgumentError("Empty string is not allowed for this term").asLeft
      case _ => nonEmptyStringParser(value)
    }
  }

  def parseTypeConstraintNonEmptyString[T](
    value: String,
    parser: String => T,
    generator: T => SearchValue,
    expectedType: String): Either[AppError, SearchValue] = {
    Try(parser(value)).map(generator(_)) match {
      case Failure(_) => InvalidArgumentError(s"'$value' is not $expectedType value").asLeft
      case Success(value) => value.asRight
    }
  }

  def parseTypeConstraintEnumString[T](
    value: String,
    parser: String => Option[T],
    expectedType: String): Either[AppError, SearchValue] = parser(value) match {
    case None => InvalidArgumentError(s"'$value' is not $expectedType value").asLeft
    case Some(value) => value.asInstanceOf[SearchValue].asRight[AppError]
  }
}
