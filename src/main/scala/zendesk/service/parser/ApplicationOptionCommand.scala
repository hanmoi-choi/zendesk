package zendesk.service.parser

sealed trait ApplicationOptionCommand

object ApplicationOptionCommand {
  case object ApplicationZendesk extends ApplicationOptionCommand
  case object ViewSearchableFields extends ApplicationOptionCommand
  case object Quit extends ApplicationOptionCommand
}
