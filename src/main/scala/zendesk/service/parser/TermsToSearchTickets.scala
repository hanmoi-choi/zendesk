package zendesk.service.parser

import java.util.UUID

import cats.syntax.either._
import org.joda.time.DateTime
import zendesk.model.AppError
import zendesk.model.value.SearchValue

sealed trait TermsToSearchTickets {
  def asSearchValue(value: String): Either[AppError, SearchValue]
}

object TermsToSearchTickets {

  import zendesk.service.parser.Parser._

  case object TicketId extends TermsToSearchTickets {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      prohibitEmptyString(value) { v =>
        parseTypeConstraintNonEmptyString[UUID](v, UUID.fromString, zendesk.model.value.TicketId(_), "UUID")
      }
  }

  case object Url extends TermsToSearchTickets {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      prohibitEmptyString(value) {
        zendesk.model.value.Url(_).asRight
      }
  }

  case object ExternalId extends TermsToSearchTickets {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      prohibitEmptyString(value) { v =>
        parseTypeConstraintNonEmptyString[UUID](v, UUID.fromString, zendesk.model.value.ExternalId(_), "UUID")
      }
  }

  case object Type extends TermsToSearchTickets {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      emptyStringAsEmptyStringSearchField(value) { v =>
        parseTypeConstraintEnumString[zendesk.model.value.Type](
          v,
          zendesk.model.value.Type.fromString,
          "Type('incident', 'problem', 'question', 'task')"
        )
      }
  }

  case object Subject extends TermsToSearchTickets {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      prohibitEmptyString(value) {
        zendesk.model.value.Subject(_).asRight
      }
  }

  case object Description extends TermsToSearchTickets {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      emptyStringAsEmptyStringSearchField(value) {
        zendesk.model.value.Description(_).asRight
      }
  }

  case object Priority extends TermsToSearchTickets {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      prohibitEmptyString(value) { v =>
        parseTypeConstraintEnumString[zendesk.model.value.Priority](
          v,
          zendesk.model.value.Priority.fromString,
          "Priority('urgent', 'high', 'normal', 'low')"
        )
      }
  }

  case object Status extends TermsToSearchTickets {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      prohibitEmptyString(value) { v =>
        parseTypeConstraintEnumString[zendesk.model.value.Status](
          v,
          zendesk.model.value.Status.fromString,
          "Status('closed', 'hold', 'open', 'pending', 'solved')"
        )
      }
  }

  case object SubmitterId extends TermsToSearchTickets {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      prohibitEmptyString(value) { v =>
        parseTypeConstraintNonEmptyString[Int](v, Integer.parseInt, zendesk.model.value.SubmitterId(_), "Integer")
      }
  }

  case object AssigneeId extends TermsToSearchTickets {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      emptyStringAsEmptyStringSearchField(value) { v =>
        parseTypeConstraintNonEmptyString[Int](v, Integer.parseInt, zendesk.model.value.AssigneeId(_), "Integer")
      }
  }

  case object OrganizationId extends TermsToSearchTickets {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      emptyStringAsEmptyStringSearchField(value) { v =>
        parseTypeConstraintNonEmptyString[Int](v, Integer.parseInt, zendesk.model.value.OrganizationId(_), "Integer")
      }
  }

  case object Tags extends TermsToSearchTickets {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      prohibitEmptyString(value) {
        zendesk.model.value.Tag(_).asRight
      }
  }

  case object HasIncidents extends TermsToSearchTickets {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      emptyStringAsEmptyStringSearchField(value) { v =>
        parseTypeConstraintNonEmptyString[Boolean](v, _.toBoolean, zendesk.model.value.HasIncidents(_), "Boolean")
      }
  }

  case object DueAt extends TermsToSearchTickets {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      emptyStringAsEmptyStringSearchField(value) { v =>
        parseTypeConstraintNonEmptyString[DateTime](
          trimWhiteSpace(v),
          DateTime.parse,
          zendesk.model.value.ZenDateTime(_),
          "DateTime")
      }
  }

  case object Via extends TermsToSearchTickets {
    override def asSearchValue(value: String): Either[AppError, SearchValue] =
      prohibitEmptyString(value) { v =>
        parseTypeConstraintEnumString[zendesk.model.value.Via](
          v,
          zendesk.model.value.Via.fromString,
          "Via('web', 'chat', 'voice')"
        )
      }
  }
}
