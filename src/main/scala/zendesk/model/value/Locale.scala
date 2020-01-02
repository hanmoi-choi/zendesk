package zendesk.model.value

import io.circe.{Decoder, Encoder}

case class Locale(value: String) extends SearchValue {
  override def rawValue: String = value
}

object Locale {
  implicit val encodeLocale: Encoder[Locale] = Encoder.encodeString.contramap[Locale](_.value)
  implicit val decodeLocale: Decoder[Locale] = Decoder.decodeString.map(Locale(_))
}
