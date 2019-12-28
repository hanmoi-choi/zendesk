package zendesk.model

import java.time.ZonedDateTime
import java.util.UUID

import io.circe._
import io.circe.syntax._
import org.specs2.mutable.Specification
import cats.implicits._

class OrganizationSpec extends Specification {
  private val createdAt = "2016-05-21T11:10:28 -10:00"
  private val rawJsonExceptCreatedAt =
    """{
      |  "_id" : 101,
      |  "url" : "http://initech.zendesk.com/api/v2/organizations/101.json",
      |  "external_id" : "9270ed79-35eb-4a38-a46f-35725197ea8d",
      |  "name" : "Enthaze",
      |  "domain_names" : [ "kage.com", "ecratic.com", "endipin.com", "zentix.com"  ],
      |  "created_at" : "%s",
      |  "details" : "MegaCorp",
      |  "shared_tickets" : false,
      |  "tags" : [ "Fulton", "West", "Rodriguez", "Farley" ]
      |}""".stripMargin.replaceAll(" ", "").replaceAll("\n", "")
  private val rawJson = String.format(rawJsonExceptCreatedAt, createdAt)
  private val domainName = List(DomainName("kage.com"), DomainName("ecratic.com"), DomainName("endipin.com"), DomainName("zentix.com"))
  private val tags = List(Tag("Fulton"), Tag("West"), Tag("Rodriguez"), Tag("Farley"))
  private val expectedOrganization = Organization(
    id = Id(101),
    url = Url("http://initech.zendesk.com/api/v2/organizations/101.json"),
    externalId = ExternalId(UUID.fromString("9270ed79-35eb-4a38-a46f-35725197ea8d")),
    name = Name("Enthaze"),
    domainNames = domainName,
    createdAt = DateTime(ZonedDateTime.parse("2016-05-21T11:10:28-10:00")),
    details = Details("MegaCorp"),
    sharedTickets = SharedTickets(false),
    tags = tags
  )


  "JSON string for a organization should be decoded to Organization object" >> {
    val result: Either[Error, Organization] = io.circe.parser.decode[Organization](rawJson)

    "result should be expected Organization Object" >> {
      result must beEqualTo(expectedOrganization.asRight)
    }
  }

  "A organization object should be encoded to Json String" >> {
    val result = expectedOrganization.asJson.noSpaces

    "result should be expected Organization Object" >> {
      result must beEqualTo(rawJson)
    }
  }
}
