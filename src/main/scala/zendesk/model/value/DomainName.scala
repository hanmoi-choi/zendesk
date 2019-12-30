package zendesk.model.value

import io.circe.{Decoder, Encoder}

case class DomainName(value: String) extends SearchValue

object DomainName {
  implicit val encodeDomainName: Encoder[DomainName] = Encoder.encodeString.contramap[DomainName](_.value)
  implicit val decodeDomainName: Decoder[DomainName] = Decoder.decodeString.map(DomainName(_))
}
