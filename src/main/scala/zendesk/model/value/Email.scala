package zendesk.model.value

import io.circe.{Decoder, Encoder}

case class Email(value: String) extends SearchValue {
  override def rawValue: String = value
}

object Email {
  implicit val encodeEmail: Encoder[Email] = Encoder.encodeString.contramap[Email](_.value)
  implicit val decodeEmail: Decoder[Email] = Decoder.decodeString.map(Email(_))
}
