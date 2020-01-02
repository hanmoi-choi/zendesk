package zendesk.model.value

import io.circe.{Decoder, Encoder}

case class Alias(value: String) extends SearchValue {
  override def rawValue: String = value
}

object Alias {
  implicit val encodeAlias: Encoder[Alias] = Encoder.encodeString.contramap[Alias](_.value)
  implicit val decodeAlias: Decoder[Alias] = Decoder.decodeString.map(Alias(_))
}
