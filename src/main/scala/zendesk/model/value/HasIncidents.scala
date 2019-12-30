package zendesk.model.value

import io.circe.{Decoder, Encoder}

case class HasIncidents(value: Boolean) extends SearchValue

object HasIncidents {
  implicit val encodeHasIncidents: Encoder[HasIncidents] = Encoder.encodeBoolean.contramap[HasIncidents](_.value)
  implicit val decodeHasIncidents: Decoder[HasIncidents] = Decoder.decodeBoolean.map(HasIncidents(_))
}
