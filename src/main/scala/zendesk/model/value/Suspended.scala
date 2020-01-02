package zendesk.model.value

import io.circe.{Decoder, Encoder}

case class Suspended(value: Boolean) extends SearchValue {
  override def rawValue: String = value.toString
}

object Suspended {
  implicit val encodeSuspended: Encoder[Suspended] = Encoder.encodeBoolean.contramap[Suspended](_.value)
  implicit val decodeSuspended: Decoder[Suspended] = Decoder.decodeBoolean.map(Suspended(_))
}
