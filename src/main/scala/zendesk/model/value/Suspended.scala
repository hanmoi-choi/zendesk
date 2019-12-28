package zendesk.model.value

import io.circe.{Decoder, Encoder}

case class Suspended(value: Boolean)

object Suspended {
  implicit val encodeSuspended: Encoder[Suspended] = Encoder.encodeBoolean.contramap[Suspended](_.value)
  implicit val decodeSuspended: Decoder[Suspended] = Decoder.decodeBoolean.map(Suspended(_))
}
