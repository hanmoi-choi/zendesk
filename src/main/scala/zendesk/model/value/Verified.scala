package zendesk.model.value

import io.circe.{Decoder, Encoder}

case class Verified(value: Boolean) extends SearchValue {
  override def rawValue: String = value.toString
}

object Verified {
  implicit val encodeVerified: Encoder[Verified] = Encoder.encodeBoolean.contramap[Verified](_.value)
  implicit val decodeVerified: Decoder[Verified] = Decoder.decodeBoolean.map(Verified(_))
}
