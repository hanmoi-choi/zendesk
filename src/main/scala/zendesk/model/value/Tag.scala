package zendesk.model.value

import io.circe.{Decoder, Encoder}

case class Tag(value: String) extends SearchValue {
  override def rawValue: String = value.toString
}

object Tag {
  implicit val encodeTag: Encoder[Tag] = Encoder.encodeString.contramap[Tag](_.value)
  implicit val decodeTag: Decoder[Tag] = Decoder.decodeString.map(Tag(_))
}
