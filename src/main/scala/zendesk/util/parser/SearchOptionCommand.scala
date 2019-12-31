package zendesk.util.parser

sealed trait SearchOptionCommand

object SearchOptionCommand {
  case object SearchZendesk extends SearchOptionCommand
  case object ViewSearchableFields extends SearchOptionCommand
  case object Quit extends SearchOptionCommand
}
