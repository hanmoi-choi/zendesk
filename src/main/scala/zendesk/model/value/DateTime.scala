package zendesk.model.value

import java.time.ZonedDateTime

import io.circe.{Decoder, Encoder}
import zendesk.model.value

case class DateTime(value: ZonedDateTime) {
  private val indexBeforeTimezoneInfo = 19

  override def toString: String = {
    val (datetime, timezone) = value.toString.splitAt(indexBeforeTimezoneInfo)
    s"$datetime $timezone"
  }
}

object DateTime {
  implicit val encodeDateTime: Encoder[DateTime] = Encoder.encodeString.contramap[DateTime](_.toString)
  implicit val decodeDateTime: Decoder[DateTime] =
    Decoder.decodeString.map(v => value.DateTime(ZonedDateTime.parse(trimWhiteSpace(v))))

  private def trimWhiteSpace(value: String): String = value.replaceAll(" ", "")
}
