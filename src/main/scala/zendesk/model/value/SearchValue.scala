package zendesk.model.value

trait SearchValue {
  def rawValue: String
}

case object EmptyStringSearchField extends SearchValue {
  override def rawValue: String = "You entered empty string"
}
