package zendesk.model.value

import io.circe.{Decoder, Encoder}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import zendesk.model.value

case class ZenDateTime(value: DateTime) {
  private val dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss")
  private val offsetFormat = DateTimeFormat.forPattern("Z")

  override def toString: String = {
    val dateTime = value.toString(dateTimeFormat)
    val (offset1, offset2) = value.toString(offsetFormat).splitAt(3)
    s"$dateTime $offset1:$offset2"
  }

  override def hashCode(): Int = toString.hashCode

  override def equals(o: Any): Boolean = toString.equals(o.toString)
}

object ZenDateTime {
  implicit val encodeDateTime: Encoder[ZenDateTime] = Encoder.encodeString.contramap[ZenDateTime](_.toString)
  implicit val decodeDateTime: Decoder[ZenDateTime] =
    Decoder.decodeString.map(v => value.ZenDateTime(DateTime.parse(trimWhiteSpace(v))))

  private def trimWhiteSpace(value: String): String = value.replaceAll(" ", "")
}
