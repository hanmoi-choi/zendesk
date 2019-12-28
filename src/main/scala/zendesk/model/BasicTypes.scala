package zendesk.model

import java.time.ZonedDateTime
import java.util.UUID

import io.circe.{Decoder, Encoder}

case class Id(value: Long)

case class Url(value: String)

case class ExternalId(value: UUID)

case class Name(value: String)

case class DomainName(value: String)

case class DateTime(value: ZonedDateTime) {
  override def toString: String = {
    val (datetime, timezone)  = value.toString.splitAt(19)
    s"$datetime $timezone"
  }
}

case class Details(value: String)

case class SharedTickets(value: Boolean)

case class Tag(value: String)

object BasicTypes {
  implicit val encodeId: Encoder[Id] = Encoder.encodeLong.contramap[Id](_.value)
  implicit val decodeId: Decoder[Id] = Decoder.decodeLong.map(Id)

  implicit val encodeUrl: Encoder[Url] = Encoder.encodeString.contramap[Url](_.value)
  implicit val decodeUrl: Decoder[Url] = Decoder.decodeString.map(Url)

  implicit val encodeExternalId: Encoder[ExternalId] = Encoder.encodeString.contramap[ExternalId](_.value.toString)
  implicit val decodeExternalId: Decoder[ExternalId] = Decoder.decodeString.map(s => ExternalId(UUID.fromString(s)))

  implicit val encodeName: Encoder[Name] = Encoder.encodeString.contramap[Name](_.value)
  implicit val decodeName: Decoder[Name] = Decoder.decodeString.map(Name)

  implicit val encodeDomainName: Encoder[DomainName] = Encoder.encodeString.contramap[DomainName](_.value)
  implicit val decodeDomainName: Decoder[DomainName] = Decoder.decodeString.map(DomainName)

  implicit val encodeCreatedAt: Encoder[DateTime] = Encoder.encodeString.contramap[DateTime](_.toString)
  implicit val decodeCreatedAt: Decoder[DateTime] = Decoder.decodeString.map(v => DateTime(ZonedDateTime.parse(v.replaceAll(" ", ""))))

  implicit val encodeDetails: Encoder[Details] = Encoder.encodeString.contramap[Details](_.value)
  implicit val decodeDetails: Decoder[Details] = Decoder.decodeString.map(Details)

  implicit val encodeSharedTickets: Encoder[SharedTickets] = Encoder.encodeBoolean.contramap[SharedTickets](_.value)
  implicit val decodeSharedTickets: Decoder[SharedTickets] = Decoder.decodeBoolean.map(SharedTickets)

  implicit val encodeTag: Encoder[Tag] = Encoder.encodeString.contramap[Tag](_.value)
  implicit val decodeTag: Decoder[Tag] = Decoder.decodeString.map(Tag)
}
