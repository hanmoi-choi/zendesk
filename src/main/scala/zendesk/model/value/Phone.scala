package zendesk.model.value

import io.circe.{Decoder, Encoder}

case class Phone(value: String) extends SearchValue {
  override def rawValue: String = value
}

object Phone {
  implicit val encodePhone: Encoder[Phone] = Encoder.encodeString.contramap[Phone](_.value)
  implicit val decodePhone: Decoder[Phone] = Decoder.decodeString.map(Phone(_))
}
