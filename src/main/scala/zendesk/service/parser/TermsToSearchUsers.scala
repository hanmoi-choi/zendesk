package zendesk.service.parser

import java.util.UUID

import cats.syntax.either._
import org.joda.time.DateTime
import zendesk.model.{AppError, ExitAppByUserRequest}
import zendesk.model.value.SearchValue

sealed trait TermsToSearchUsers {
  def asSearchValue(value: String): Either[AppError, SearchValue]
}

object TermsToSearchUsers {

  import zendesk.service.parser.Parser._

  case object Id extends TermsToSearchUsers {
    override def asSearchValue(value: String): Either[AppError, SearchValue] = {
      prohibitEmptyString(value) { v =>
        parseTypeConstraintNonEmptyString[Int](v, Integer.parseInt, zendesk.model.value.Id(_), "Integer")
      }
    }
  }

  case object Url extends TermsToSearchUsers {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      prohibitEmptyString(value) {
        zendesk.model.value.Url(_).asRight
      }
  }

  case object ExternalId extends TermsToSearchUsers {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      prohibitEmptyString(value) { v =>
        parseTypeConstraintNonEmptyString[UUID](v, UUID.fromString, zendesk.model.value.ExternalId(_), "UUID")
      }
  }

  case object Name extends TermsToSearchUsers {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      prohibitEmptyString(value) {
        zendesk.model.value.Name(_).asRight
      }
  }

  case object Alias extends TermsToSearchUsers {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      emptyStringAsEmptyStringSearchField(value) {
        zendesk.model.value.Alias(_).asRight
      }
  }

  case object CreatedAt extends TermsToSearchUsers {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      prohibitEmptyString(value) { v =>
        parseTypeConstraintNonEmptyString[DateTime](
          trimWhiteSpace(v),
          DateTime.parse,
          zendesk.model.value.ZenDateTime(_),
          "DateTime")
      }
  }

  case object Active extends TermsToSearchUsers {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      prohibitEmptyString(value) { v =>
        parseTypeConstraintNonEmptyString[Boolean](v, _.toBoolean, zendesk.model.value.Active(_), "Boolean")
      }
  }

  case object Verified extends TermsToSearchUsers {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      emptyStringAsEmptyStringSearchField(value) { v =>
        parseTypeConstraintNonEmptyString[Boolean](v, _.toBoolean, zendesk.model.value.Verified(_), "Boolean")
      }
  }

  case object Shared extends TermsToSearchUsers {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      prohibitEmptyString(value) { v =>
        parseTypeConstraintNonEmptyString[Boolean](v, _.toBoolean, zendesk.model.value.Shared(_), "Boolean")
      }

  }

  case object Locale extends TermsToSearchUsers {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      emptyStringAsEmptyStringSearchField(value) {
        zendesk.model.value.Locale(_).asRight
      }
  }

  case object Timezone extends TermsToSearchUsers {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      emptyStringAsEmptyStringSearchField(value) {
        zendesk.model.value.Timezone(_).asRight
      }
  }

  case object LastLoginAt extends TermsToSearchUsers {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      prohibitEmptyString(value) { v =>
        parseTypeConstraintNonEmptyString[DateTime](
          trimWhiteSpace(v),
          DateTime.parse,
          zendesk.model.value.ZenDateTime(_),
          "DateTime")
      }
  }

  case object Email extends TermsToSearchUsers {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      emptyStringAsEmptyStringSearchField(value) {
        zendesk.model.value.Email(_).asRight
      }
  }

  case object Phone extends TermsToSearchUsers {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      prohibitEmptyString(value) {
        zendesk.model.value.Phone(_).asRight
      }
  }

  case object Signature extends TermsToSearchUsers {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      prohibitEmptyString(value) {
        zendesk.model.value.Signature(_).asRight
      }
  }

  case object OrganizationId extends TermsToSearchUsers {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      emptyStringAsEmptyStringSearchField(value) { v =>
        parseTypeConstraintNonEmptyString[Int](v, Integer.parseInt, zendesk.model.value.Id(_), "Integer")
      }
  }

  case object Tags extends TermsToSearchUsers {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      prohibitEmptyString(value) {
        zendesk.model.value.Tag(_).asRight
      }
  }

  case object Suspended extends TermsToSearchUsers {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      prohibitEmptyString(value) { v =>
        parseTypeConstraintNonEmptyString[Boolean](v, _.toBoolean, zendesk.model.value.Suspended(_), "Boolean")
      }
  }

  case object Role extends TermsToSearchUsers {
    override def asSearchValue(value: String): Either[AppError, SearchValue] = {
      prohibitEmptyString(value) { v =>
        parseTypeConstraintEnumString[zendesk.model.value.Role](
          v,
          zendesk.model.value.Role.fromString,
          "Role('admin', 'agent', 'end-user')"
        )
      }
    }
  }

  case object Quit extends TermsToSearchUsers {
    override def asSearchValue(value: String): Either[AppError, SearchValue] = ExitAppByUserRequest.asLeft
  }
}
