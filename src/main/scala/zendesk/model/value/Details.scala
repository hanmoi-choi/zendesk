package zendesk.model.value

import io.circe.{Decoder, Encoder}

case class Details(value: String) extends SearchValue {
  override def rawValue: String = value
}

object Details {
  implicit val encodeDetails: Encoder[Details] = Encoder.encodeString.contramap[Details](_.value)
  implicit val decodeDetails: Decoder[Details] = Decoder.decodeString.map(Details(_))
}
