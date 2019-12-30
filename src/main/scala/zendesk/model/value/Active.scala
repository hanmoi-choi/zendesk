package zendesk.model.value

import io.circe.{Decoder, Encoder}

case class Active(value: Boolean) extends SearchValue

object Active {
  implicit val encodeActive: Encoder[Active] = Encoder.encodeBoolean.contramap[Active](_.value)
  implicit val decodeActive: Decoder[Active] = Decoder.decodeBoolean.map(Active(_))
}
