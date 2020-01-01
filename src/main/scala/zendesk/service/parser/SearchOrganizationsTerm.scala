package zendesk.service.parser

import java.util.UUID

import cats.syntax.either._
import org.joda.time.DateTime
import zendesk.model.AppError
import zendesk.model.value.SearchValue

sealed trait SearchOrganizationsTerm {
  def asSearchValue(value: String): Either[AppError, SearchValue]
}

object SearchOrganizationsTerm {
  import zendesk.service.parser.Parser._

  case object Id extends SearchOrganizationsTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      prohibitEmptyString(value) { v =>
        parseTypeConstraintNonEmptyString[Int](v, Integer.parseInt, zendesk.model.value.Id(_), "Integer")
      }
  }

  case object Url extends SearchOrganizationsTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      prohibitEmptyString(value) {
        zendesk.model.value.Url(_).asRight
      }
  }

  case object ExternalId extends SearchOrganizationsTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      prohibitEmptyString(value) { v =>
        parseTypeConstraintNonEmptyString[UUID](v, UUID.fromString, zendesk.model.value.ExternalId(_), "UUID")
      }
  }

  case object Name extends SearchOrganizationsTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      prohibitEmptyString(value) {
        zendesk.model.value.Name(_).asRight
      }
  }

  case object DomainNames extends SearchOrganizationsTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      prohibitEmptyString(value) {
        zendesk.model.value.DomainName(_).asRight
      }
  }

  case object CreatedAt extends SearchOrganizationsTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      prohibitEmptyString(value) { v =>
        parseTypeConstraintNonEmptyString[DateTime](trimWhiteSpace(v), DateTime.parse, zendesk.model.value.ZenDateTime(_), "DateTime" )
      }
  }

  case object Details extends SearchOrganizationsTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      prohibitEmptyString(value) {
        zendesk.model.value.Details(_).asRight
      }
  }

  case object SharedTickets extends SearchOrganizationsTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      prohibitEmptyString(value) { v =>
        parseTypeConstraintNonEmptyString[Boolean](v, _.toBoolean, zendesk.model.value.SharedTickets(_), "Boolean" )
      }
  }

  case object Tags extends SearchOrganizationsTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      prohibitEmptyString(value) {
        zendesk.model.value.Tag(_).asRight
      }
  }

  case object Quit extends SearchOrganizationsTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] = ???
  }

}
