package zendesk.model.value

import io.circe.{Decoder, Encoder}
import cats.syntax.option._

/*
❯ cat tickets.json |jq 'map(.status) | unique'
[
  "closed",
  "hold",
  "open",
  "pending",
  "solved"
]
 */
sealed case class Status(name: String) extends SearchValue

object Closed extends Status("closed")

object Hold extends Status("hold")

object Open extends Status("open")

object Pending extends Status("pending")

object Solved extends Status("solved")

object Status {
  val all = List(Closed, Hold, Open, Pending, Solved)

  def fromString(value: String): Option[Status] = {
    value.toLowerCase() match {
      case "closed" => Closed.some
      case "hold" => Hold.some
      case "open" => Open.some
      case "pending" => Pending.some
      case "solved" => Solved.some
      case _ => None
    }
  }

  implicit val encoderEvent: Encoder[Status] = {
    Encoder[String].contramap(_.name)
  }

  implicit val decoderEvent: Decoder[Status] = {
    Decoder[String].map { name =>
      all.find(_.name == name).getOrElse(Status(name))
    }
  }
}
