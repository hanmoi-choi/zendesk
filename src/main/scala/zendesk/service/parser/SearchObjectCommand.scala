package zendesk.service.parser

sealed trait SearchObjectCommand

object SearchObjectCommand {

  case object SearchUsers extends SearchObjectCommand

  case object SearchTickets extends SearchObjectCommand

  case object SearchOrganizations extends SearchObjectCommand

}
