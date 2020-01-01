package zendesk.model

sealed trait AppError

case class ParseFailure(message: String) extends AppError

case class InvalidArgumentError(message: String) extends AppError


