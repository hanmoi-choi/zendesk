package zendesk.model.value

import io.circe.{Decoder, Encoder}

case class SharedTickets(value: Boolean) extends SearchValue

object SharedTickets {
  implicit val encodeSharedTickets: Encoder[SharedTickets] = Encoder.encodeBoolean.contramap[SharedTickets](_.value)
  implicit val decodeSharedTickets: Decoder[SharedTickets] = Decoder.decodeBoolean.map(SharedTickets(_))
}
