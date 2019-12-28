package zendesk.model.value

import io.circe.{Decoder, Encoder}

case class Signature(value: String)

object Signature {
  implicit val encodeSignature: Encoder[Signature] = Encoder.encodeString.contramap[Signature](_.value)
  implicit val decodeSignature: Decoder[Signature] = Decoder.decodeString.map(Signature(_))
}
