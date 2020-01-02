package zendesk.model.value

import io.circe.{Decoder, Encoder}

case class Shared(value: Boolean) extends SearchValue {
  override def rawValue: String = value.toString
}

object Shared {
  implicit val encodeShared: Encoder[Shared] = Encoder.encodeBoolean.contramap[Shared](_.value)
  implicit val decodeShared: Decoder[Shared] = Decoder.decodeBoolean.map(Shared(_))
}
