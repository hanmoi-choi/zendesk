package zendesk.model

sealed trait AppError

case class ParseFailure(command: String) extends AppError


