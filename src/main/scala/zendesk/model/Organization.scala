package zendesk.model

import io.circe.syntax._
import io.circe.{Decoder, Encoder, HCursor, Json}
import zendesk.model.value._

case class Organization(
                         id: Id,
                         url: Url,
                         externalId: ExternalId,
                         name: Name,
                         domainNames: List[DomainName],
                         createdAt: ZenDateTime,
                         details: Details,
                         sharedTickets: SharedTickets,
                         tags: List[Tag]
                       ) extends Searchable {

  def pairWithTag(): List[(Tag, Organization)] = tags.map((_, this))

  def pairWithDomainName(): List[(DomainName, Organization)] = domainNames.map((_, this))
}

object Organization {
  implicit val encodeOrg: Encoder[Organization] = (org: Organization) => Json.obj(
    ("_id", org.id.asJson),
    ("url", org.url.asJson),
    ("external_id", org.externalId.asJson),
    ("name", org.name.asJson),
    ("domain_names", org.domainNames.asJson),
    ("created_at", org.createdAt.asJson),
    ("details", org.details.asJson),
    ("shared_tickets", org.sharedTickets.asJson),
    ("tags", org.tags.asJson)
  )

  implicit val decodeOrg: Decoder[Organization] = (c: HCursor) => for {
    id <- c.downField("_id").as[Id]
    url <- c.downField("url").as[Url]
    externalId <- c.downField("external_id").as[ExternalId]
    name <- c.downField("name").as[Name]
    domainNames <- c.downField("domain_names").as[List[DomainName]]
    createdAt <- c.downField("created_at").as[ZenDateTime]
    details <- c.downField("details").as[Details]
    sharedTickets <- c.downField("shared_tickets").as[SharedTickets]
    tags <- c.downField("tags").as[List[Tag]]
  } yield {
    new Organization(
      id = id,
      url = url,
      externalId = externalId,
      name = name,
      domainNames = domainNames,
      createdAt = createdAt,
      details = details,
      sharedTickets = sharedTickets,
      tags = tags
    )
  }
}
