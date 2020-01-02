package zendesk.model.value

import io.circe.{Decoder, Encoder}

case class Description(value: String) extends SearchValue {
  override def rawValue: String = value
}

object Description {
  implicit val encodeDescription: Encoder[Description] = Encoder.encodeString.contramap[Description](_.value)
  implicit val decodeDescription: Decoder[Description] = Decoder.decodeString.map(Description(_))
}
