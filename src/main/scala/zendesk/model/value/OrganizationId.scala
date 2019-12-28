package zendesk.model.value

import io.circe.{Decoder, Encoder}

case class OrganizationId(value: Long)

object OrganizationId {
  implicit val encodeOrganizationId: Encoder[OrganizationId] = Encoder.encodeLong.contramap[OrganizationId](_.value)
  implicit val decodeOrganizationId: Decoder[OrganizationId] = Decoder.decodeLong.map(OrganizationId(_))
}
