package zendesk.model.value

import io.circe.{Decoder, Encoder}

case class Locale(value: String)

object Locale {
  implicit val encodeLocale: Encoder[Locale] = Encoder.encodeString.contramap[Locale](_.value)
  implicit val decodeLocale: Decoder[Locale] = Decoder.decodeString.map(Locale(_))
}
