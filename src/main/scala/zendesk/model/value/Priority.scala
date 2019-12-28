package zendesk.model.value

import io.circe.{Decoder, Encoder}

/*
❯ cat tickets.json |jq 'map(.priority) | unique'
[
  "high",
  "low",
  "normal",
  "urgent"
]
 */
sealed case class Priority(name: String)

object Urgent extends Priority("urgent")

object High extends Priority("high")

object Normal extends Priority("normal")

object Low extends Priority("low")

object Priority {
  val all = List(Urgent, High, Normal, Low)

  implicit val encoderEvent: Encoder[Priority] = {
    Encoder[String].contramap(_.name)
  }

  implicit val decoderEvent: Decoder[Priority] = {
    Decoder[String].map { name =>
      all.find(_.name == name).getOrElse(Priority(name))
    }
  }
}
