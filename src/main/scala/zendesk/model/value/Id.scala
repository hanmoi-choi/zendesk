package zendesk.model.value

import io.circe.{Decoder, Encoder}

case class Id(value: Long)

object Id {
  implicit val encodeId: Encoder[Id] = Encoder.encodeLong.contramap[Id](_.value)
  implicit val decodeId: Decoder[Id] = Decoder.decodeLong.map(Id(_))
}
