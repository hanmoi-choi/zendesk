package zendesk.model.value

sealed trait SearchObject
object SearchObject {
  case object User extends SearchObject
  case object Organization extends SearchObject
  case object Ticket extends SearchObject
}

case class QueryParams(searchObject: SearchObject, searchTerm: String, searchValue: SearchValue)
