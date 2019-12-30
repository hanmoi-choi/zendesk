package zendesk.model.value

import io.circe.{Decoder, Encoder}

case class SubmitterId(value: Long) extends SearchValue

object SubmitterId {
  implicit val encodeSubmitterId: Encoder[SubmitterId] = Encoder.encodeLong.contramap[SubmitterId](_.value)
  implicit val decodeSubmitterId: Decoder[SubmitterId] = Decoder.decodeLong.map(SubmitterId(_))
}
