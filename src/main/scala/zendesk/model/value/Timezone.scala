package zendesk.model.value

import io.circe.{Decoder, Encoder}

case class Timezone(value: String) extends SearchValue {
  override def rawValue: String = value.toString
}

object Timezone {

  implicit val encodeTimezone: Encoder[Timezone] = Encoder.encodeString.contramap[Timezone](_.value)
  implicit val decodeTimezone: Decoder[Timezone] = Decoder.decodeString.map(Timezone(_))
}
