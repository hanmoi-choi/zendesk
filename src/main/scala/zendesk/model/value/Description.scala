package zendesk.model.value

import io.circe.{Decoder, Encoder}

case class Description(value: String) extends SearchValue

object Description {
  implicit val encodeDescription: Encoder[Description] = Encoder.encodeString.contramap[Description](_.value)
  implicit val decodeDescription: Decoder[Description] = Decoder.decodeString.map(Description(_))
}
