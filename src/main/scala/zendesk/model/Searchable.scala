package zendesk.model

trait Searchable {
  def asFullDataString: String
  def asSimpleDataString: String
  def trimJsonString(value: String): String =
    value
      .replaceAll("\"", "")
      .replaceAll(",", "")
      .replaceAll("\\}", "")
      .trim
      .replaceAll("\\{", "")
}

object Searchable {
  sealed trait Keys

  case object Users extends Keys

  case object Tickets extends Keys

  case object Organizations extends Keys

}
