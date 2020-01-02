package zendesk.model.value

import io.circe.{Decoder, Encoder}

case class Subject(value: String) extends SearchValue {
  override def rawValue: String = value
}

object Subject {
  implicit val encodeSubject: Encoder[Subject] = Encoder.encodeString.contramap[Subject](_.value)
  implicit val decodeSubject: Decoder[Subject] = Decoder.decodeString.map(Subject(_))
}
