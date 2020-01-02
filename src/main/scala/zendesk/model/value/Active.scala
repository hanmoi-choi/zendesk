package zendesk.model.value

import io.circe.{Decoder, Encoder}

case class Active(value: Boolean) extends SearchValue {
  override def rawValue: String = value.toString
}

object Active {
  implicit val encodeActive: Encoder[Active] = Encoder.encodeBoolean.contramap[Active](_.value)
  implicit val decodeActive: Decoder[Active] = Decoder.decodeBoolean.map(Active(_))
}
