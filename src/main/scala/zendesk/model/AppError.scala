package zendesk.model

import zendesk.util.MessageFactory

sealed trait AppError

case class ParseFailure(message: String) extends AppError

case class InvalidArgumentError(message: String) extends AppError

case class DataNotfound(message: String) extends AppError

case class FileNotExistError(message: String) extends AppError

case object ExitAppByUserRequest extends AppError

object AppError {
  def asText(error: AppError): String = error match {
    case ExitAppByUserRequest => MessageFactory.goodbye
    case ParseFailure(message) => s"[Parse Failed]: $message"
    case InvalidArgumentError(message) => s"[Invalid input]: $message"
    case FileNotExistError(message) => s"[File Not Exist] $message"
    case DataNotfound(message) => s"[No data found]: $message"
  }
}
