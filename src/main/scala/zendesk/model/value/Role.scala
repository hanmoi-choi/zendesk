package zendesk.model.value

import io.circe.{Decoder, Encoder}
import cats.syntax.option._
/*
❯ cat users.json |jq 'map(.role) | unique'
[
  "admin",
  "agent",
  "end-user"
]
 */
sealed case class Role(name: String) extends SearchValue

object Admin extends Role("admin")

object EndUser extends Role("end-user")

object Agent extends Role("agent")

object Role {
  val all = List(Admin, EndUser, Agent)

  def fromString(value: String): Option[Role] = {
    value match {
      case "admin" => Admin.some
      case "end-user" => EndUser.some
      case "agent" => Agent.some
      case _ => None
    }
  }

  implicit val encoderEvent: Encoder[Role] = {
    Encoder[String].contramap(_.name)
  }

  implicit val decoderEvent: Decoder[Role] = {
    Decoder[String].map { name =>
      all.find(_.name == name).getOrElse(Role(name))
    }
  }
}