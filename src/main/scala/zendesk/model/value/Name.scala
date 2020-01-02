package zendesk.model.value

import io.circe.{Decoder, Encoder}

case class Name(value: String) extends SearchValue {
  override def rawValue: String = value
}

object Name {
  implicit val encodeName: Encoder[Name] = Encoder.encodeString.contramap[Name](_.value)
  implicit val decodeName: Decoder[Name] = Decoder.decodeString.map(Name(_))
}
