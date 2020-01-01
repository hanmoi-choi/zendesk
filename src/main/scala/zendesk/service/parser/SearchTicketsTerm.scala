package zendesk.service.parser

import zendesk.model.AppError
import zendesk.model.value.SearchValue

sealed trait SearchTicketsTerm {
  def asSearchValue(value: String): Either[AppError, SearchValue]
}

object SearchTicketsTerm {
  import zendesk.service.parser.Parser._

  case object Id extends SearchTicketsTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] = ???
  }
  case object Url extends SearchTicketsTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] = ???
  }
  case object ExternalId extends SearchTicketsTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] = ???
  }
  case object Type extends SearchTicketsTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] = ???
  }
  case object Subject extends SearchTicketsTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] = ???
  }
  case object Description extends SearchTicketsTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] = ???
  }
  case object Priority extends SearchTicketsTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] = ???
  }
  case object Status extends SearchTicketsTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] = ???
  }
  case object SubmitterId extends SearchTicketsTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] = ???
  }
  case object AssigneeId extends SearchTicketsTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] = ???
  }
  case object OrganizationId extends SearchTicketsTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] = ???
  }
  case object Tags extends SearchTicketsTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] = ???
  }
  case object HasIncidents extends SearchTicketsTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] = ???
  }
  case object DueAt extends SearchTicketsTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] = ???
  }
  case object Via extends SearchTicketsTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] = ???
  }
  case object Quit extends SearchTicketsTerm {
    override def asSearchValue(value: String): Either[AppError, SearchValue] = ???
  }
}



