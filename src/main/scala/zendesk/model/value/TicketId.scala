package zendesk.model.value

import java.util.UUID

import io.circe.{Decoder, Encoder}
import zendesk.model.value

case class TicketId(value: UUID)

object TicketId {
  implicit val encodeTicketId: Encoder[TicketId] = Encoder.encodeString.contramap[TicketId](_.value.toString)
  implicit val decodeTicketId: Decoder[TicketId] = Decoder.decodeString.map(s => value.TicketId(UUID.fromString(s)))

}
