package zendesk.model.value

import java.util.UUID

import io.circe.{Decoder, Encoder}
import zendesk.model.value

case class ExternalId(value: UUID)

object ExternalId {
  implicit val encodeExternalId: Encoder[ExternalId] = Encoder.encodeString.contramap[ExternalId](_.value.toString)
  implicit val decodeExternalId: Decoder[ExternalId] = Decoder.decodeString.map(s => value.ExternalId(UUID.fromString(s)))

}
