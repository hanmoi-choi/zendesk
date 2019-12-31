package zendesk.util.parser

sealed trait SearchOrganizationsTerm

object SearchOrganizationsTerm {
  case object Id extends SearchOrganizationsTerm
  case object Url extends SearchOrganizationsTerm
  case object ExternalId extends SearchOrganizationsTerm
  case object Name extends SearchOrganizationsTerm
  case object DomainNames extends SearchOrganizationsTerm
  case object CreatedAt extends SearchOrganizationsTerm
  case object Details extends SearchOrganizationsTerm
  case object SharedTickets extends SearchOrganizationsTerm
  case object Tags extends SearchOrganizationsTerm
  case object Quit extends SearchOrganizationsTerm
}
