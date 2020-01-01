package zendesk.model

import io.circe.syntax._
import io.circe.{Decoder, Encoder, HCursor, Json}
import zendesk.model.Searchable.SearchKey
import zendesk.model.value._

case class User(
                 id: Id,
                 url: Url,
                 externalId: ExternalId,
                 name: Name,
                 alias: Option[Alias],
                 createdAt: ZenDateTime,
                 active: Active,
                 verified: Option[Verified],
                 shared: Shared,
                 locale: Option[Locale],
                 timezone: Option[Timezone],
                 lastLoginAt: ZenDateTime,
                 email: Option[Email],
                 phone: Phone,
                 signature: Signature,
                 organizationId: Option[OrganizationId],
                 tags: List[Tag],
                 suspended: Suspended,
                 role: Role
               ) extends Searchable {

  def pairWithTag(): List[(Tag, User)] = tags.map((_, this))

}

object User {

  def searchKey: SearchKey = "Users"

  implicit val encodeUser: Encoder[User] = (user: User) => Json.obj(
    ("_id", user.id.asJson),
    ("url", user.url.asJson),
    ("external_id", user.externalId.asJson),
    ("name", user.name.asJson),
    ("alias", user.alias.asJson),
    ("created_at", user.createdAt.asJson),
    ("active", user.active.asJson),
    ("verified", user.verified.asJson),
    ("shared", user.shared.asJson),
    ("locale", user.locale.asJson),
    ("timezone", user.timezone.asJson),
    ("last_login_at", user.lastLoginAt.asJson),
    ("email", user.email.asJson),
    ("phone", user.phone.asJson),
    ("signature", user.signature.asJson),
    ("organization_id", user.organizationId.asJson),
    ("tags", user.tags.asJson),
    ("suspended", user.suspended.asJson),
    ("role", user.role.asJson),
  )

  implicit val decodeUser: Decoder[User] = (c: HCursor) => for {
    id <- c.downField("_id").as[Id]
    url <- c.downField("url").as[Url]
    externalId <- c.downField("external_id").as[ExternalId]
    name <- c.downField("name").as[Name]
    alias <- c.downField("alias").as[Option[Alias]]
    createdAt <- c.downField("created_at").as[ZenDateTime]
    active <- c.downField("active").as[Active]
    verified <- c.downField("verified").as[Option[Verified]]
    shared <- c.downField("shared").as[Shared]
    locale <- c.downField("locale").as[Option[Locale]]
    timezone <- c.downField("timezone").as[Option[Timezone]]
    lastLoginAt <- c.downField("last_login_at").as[ZenDateTime]
    email <- c.downField("email").as[Option[Email]]
    phone <- c.downField("phone").as[Phone]
    signature <- c.downField("signature").as[Signature]
    organizationId <- c.downField("organization_id").as[Option[OrganizationId]]
    tags <- c.downField("tags").as[List[Tag]]
    suspended <- c.downField("suspended").as[Suspended]
    role <- c.downField("role").as[Role]
  } yield {
    new User(
      id = id,
      url = url,
      externalId = externalId,
      name = name,
      alias = alias,
      createdAt = createdAt,
      active = active,
      verified = verified,
      shared = shared,
      locale = locale,
      timezone = timezone,
      lastLoginAt = lastLoginAt,
      email = email,
      phone = phone,
      signature = signature,
      organizationId = organizationId,
      tags = tags,
      suspended = suspended,
      role = role
    )
  }
}