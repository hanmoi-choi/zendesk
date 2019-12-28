package zendesk.model

import java.time.ZonedDateTime
import java.util.UUID

import io.circe._
import io.circe.syntax._
import cats.syntax.either._
import org.specs2.mutable.Specification
import zendesk.model.value._

class UserSpec  extends Specification {
  private val rawJson =
    """{
      |  "_id" : 1,
      |  "url" : "http://initech.zendesk.com/api/v2/users/1.json",
      |  "external_id" : "74341f74-9c79-49d5-9611-87ef9b6eb75f",
      |  "name" : "Francisca Rasmussen",
      |  "alias" : "Miss Coffey",
      |  "created_at" : "2016-05-21T11:10:28 -10:00",
      |  "active" : true,
      |  "verified" : true,
      |  "shared" : false,
      |  "locale" : "en-AU",
      |  "timezone" : "Sri Lanka",
      |  "last_login_at" : "2016-05-21T11:10:28 -10:00",
      |  "email" : "coffeyrasmussen@flotonic.com",
      |  "phone" : "8335-422-718",
      |  "signature" : "Don't Worry Be Happy!",
      |  "organization_id" : 119,
      |  "tags" : [
      |    "Springville",
      |    "Sutton",
      |    "Diaperville"
      |  ],
      |  "suspended" : true,
      |  "role" : "admin"
      |}""".stripMargin
  private val tags = List(Tag("Springville"), Tag("Sutton"), Tag("Diaperville"))
  private val expectedUser = User(
    id = Id(1),
    url = Url("http://initech.zendesk.com/api/v2/users/1.json"),
    externalId = ExternalId(UUID.fromString("74341f74-9c79-49d5-9611-87ef9b6eb75f")),
    name = Name("Francisca Rasmussen"),
    alias = Alias("Miss Coffey"),
    createdAt = DateTime(ZonedDateTime.parse("2016-05-21T11:10:28-10:00")),
    active = Active(true),
    verified = Verified(true),
    shared = Shared(false),
    locale = Locale("en-AU"),
    timezone = Timezone("Sri Lanka"),
    lastLoginAt = DateTime(ZonedDateTime.parse("2016-05-21T11:10:28-10:00")),
    email = Email("coffeyrasmussen@flotonic.com"),
    phone = Phone("8335-422-718"),
    signature = Signature("Don't Worry Be Happy!"),
    organizationId = OrganizationId(119),
    tags = tags,
    suspended = Suspended(true),
    role = Admin
  )


  "JSON string for a organization should be decoded to Organization object" >> {
    val result: Either[Error, User] = io.circe.parser.decode[User](rawJson)

    "result should be expected Organization Object" >> {
      result must beEqualTo(expectedUser.asRight)
    }
  }

  "A organization object should be encoded to Json String" >> {
    val result = expectedUser.asJson.spaces2

    "result should be expected Organization Object" >> {
      result must beEqualTo(rawJson)
    }
  }
}