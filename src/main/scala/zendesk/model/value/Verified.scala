package zendesk.model.value

import io.circe.{Decoder, Encoder}

case class Verified(value: Boolean)

object Verified {
  implicit val encodeVerified: Encoder[Verified] = Encoder.encodeBoolean.contramap[Verified](_.value)
  implicit val decodeVerified: Decoder[Verified] = Decoder.decodeBoolean.map(Verified(_))
}
