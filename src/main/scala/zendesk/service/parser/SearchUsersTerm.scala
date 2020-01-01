package zendesk.service.parser

import zendesk.model.value.{EmptyStringSearchField, SearchValue}
import zendesk.model.{AppError, InvalidArgumentError}

import scala.util.{Failure, Success, Try}
import cats.syntax.either._

import scala.util.Try

sealed trait SearchUsersTerm {
  def asSearchValue(value: String): Either[AppError, SearchValue]
}

object SearchUsersTerm {

  private def emptyStringAsEmptyStringSearchField(value: String)
                                 (
                                   nonEmptyStringParser: String => Either[AppError, SearchValue]
                                 ): Either[AppError, SearchValue] = {
    value match {
      case "" => EmptyStringSearchField.asRight
      case _ => nonEmptyStringParser(value)
    }
  }

  private def prohibitEmptyString(value: String)
                                 (
                                   nonEmptyStringParser: String => Either[AppError, SearchValue]
                                 ): Either[AppError, SearchValue] = {
    value match {
      case "" => InvalidArgumentError("Empty string is not allowed for this term").asLeft
      case _ => nonEmptyStringParser(value)
    }
  }

  private def parseTypeConstraintNonEmptyString[T](value: String,
                                            parser: String => T,
                                            generator: T => SearchValue,
                                            expectedType: String
                                           ): Either[AppError, SearchValue] = {
      Try(parser(value)).map(generator(_)) match {
        case Failure(_) => InvalidArgumentError(s"'$value' is not $expectedType value").asLeft
        case Success(value) => value.asRight
      }
  }

  private def parseTypeConstraintEnumString[T](value: String,
                                                   parser: String => Option[T],
                                                   expectedType: String
                                                  ): Either[AppError, SearchValue] = {
    parser(value) match {
      case None => InvalidArgumentError(s"'$value' is not $expectedType value").asLeft
      case Some(value: SearchValue) => value.asRight
    }
  }

  case object Id extends SearchUsersTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] = {
      prohibitEmptyString(value) { v =>
        parseTypeConstraintNonEmptyString[Int](v, Integer.parseInt, zendesk.model.value.Id(_), "Integer")
      }
    }
  }

  case object Url extends SearchUsersTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      prohibitEmptyString(value) {
        zendesk.model.value.Url(_).asRight
      }
  }

  case object ExternalId extends SearchUsersTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] = ???
  }

  case object Name extends SearchUsersTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      prohibitEmptyString(value) {
        zendesk.model.value.Name(_).asRight
      }
  }

  case object Alias extends SearchUsersTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      emptyStringAsEmptyStringSearchField(value) {
        zendesk.model.value.Alias(_).asRight
      }
  }

  case object CreatedAt extends SearchUsersTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] = ???
  }

  case object Active extends SearchUsersTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      prohibitEmptyString(value) { v =>
        parseTypeConstraintNonEmptyString[Boolean](v, _.toBoolean, zendesk.model.value.Active(_), "Boolean" )
      }
  }

  case object Verified extends SearchUsersTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      emptyStringAsEmptyStringSearchField(value) { v =>
        parseTypeConstraintNonEmptyString[Boolean](v, _.toBoolean, zendesk.model.value.Verified(_), "Boolean" )
      }
  }

  case object Shared extends SearchUsersTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      prohibitEmptyString(value) { v =>
        parseTypeConstraintNonEmptyString[Boolean](v, _.toBoolean, zendesk.model.value.Shared(_), "Boolean" )
      }

  }

  case object Locale extends SearchUsersTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      emptyStringAsEmptyStringSearchField(value) {
        zendesk.model.value.Locale(_).asRight
      }
  }

  case object Timezone extends SearchUsersTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      emptyStringAsEmptyStringSearchField(value) {
        zendesk.model.value.Timezone(_).asRight
      }

  }

  case object LastLoginAt extends SearchUsersTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] = ???
  }

  case object Email extends SearchUsersTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      emptyStringAsEmptyStringSearchField(value) {
        zendesk.model.value.Email(_).asRight
      }
  }

  case object Phone extends SearchUsersTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      prohibitEmptyString(value) {
        zendesk.model.value.Phone(_).asRight
      }
  }

  case object Signature extends SearchUsersTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      prohibitEmptyString(value) {
        zendesk.model.value.Signature(_).asRight
      }
  }

  case object OrganizationId extends SearchUsersTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      emptyStringAsEmptyStringSearchField(value) { v =>
        parseTypeConstraintNonEmptyString[Int](v, Integer.parseInt, zendesk.model.value.Id(_), "Integer" )
      }
  }

  case object Tags extends SearchUsersTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      prohibitEmptyString(value) {
        zendesk.model.value.Tag(_).asRight
      }
  }

  case object Suspended extends SearchUsersTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      prohibitEmptyString(value) { v =>
        parseTypeConstraintNonEmptyString[Boolean](v, _.toBoolean, zendesk.model.value.Suspended(_), "Boolean" )
      }
  }

  case object Role extends SearchUsersTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] = {
      prohibitEmptyString(value) { v =>
        parseTypeConstraintEnumString[zendesk.model.value.Role](
          v, zendesk.model.value.Role.fromString, "Role('admin', 'agent', 'end-user')"
        )
      }
    }
  }

  case object Quit extends SearchUsersTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] = ???
  }
}

