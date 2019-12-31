package zendesk.util.parser

sealed trait SearchTicketsTerm

object SearchTicketsTerm {
  case object Id extends SearchTicketsTerm
  case object Url extends SearchTicketsTerm
  case object ExternalId extends SearchTicketsTerm
  case object Type extends SearchTicketsTerm
  case object Subject extends SearchTicketsTerm
  case object Description extends SearchTicketsTerm
  case object Priority extends SearchTicketsTerm
  case object Status extends SearchTicketsTerm
  case object SubmitterId extends SearchTicketsTerm
  case object AssigneeId extends SearchTicketsTerm
  case object OrganizationId extends SearchTicketsTerm
  case object Tags extends SearchTicketsTerm
  case object HasIncidents extends SearchTicketsTerm
  case object DueAt extends SearchTicketsTerm
  case object Via extends SearchTicketsTerm
  case object Quit extends SearchTicketsTerm
}



