package zendesk.model.value

import io.circe.{Decoder, Encoder}

case class Url(value: String) extends SearchValue {
  override def rawValue: String = value
}

object Url {
  implicit val encodeUrl: Encoder[Url] = Encoder.encodeString.contramap[Url](_.value)
  implicit val decodeUrl: Decoder[Url] = Decoder.decodeString.map(Url(_))
}
