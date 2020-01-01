package zendesk.model

trait Searchable {
}

object Searchable {
  type SearchKey = String

  sealed trait Keys

  case object Users extends Keys

  case object Tickets extends Keys

  case object Organizations extends Keys

}
