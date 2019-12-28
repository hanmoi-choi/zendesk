package zendesk.model.value

import io.circe.{Decoder, Encoder}

/*
❯ cat tickets.json |jq 'map(.via) | unique'
[
  "chat",
  "voice",
  "web"
]
 */
sealed case class Via(name: String)

object Web extends Via("web")

object Chat extends Via("chat")

object Voice extends Via("voice")

object Via {
  val all = List(Web, Chat, Voice)

  implicit val encoderEvent: Encoder[Via] = {
    Encoder[String].contramap(_.name)
  }

  implicit val decoderEvent: Decoder[Via] = {
    Decoder[String].map { name =>
      all.find(_.name == name).getOrElse(Via(name))
    }
  }
}
