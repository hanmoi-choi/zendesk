package zendesk.model

import java.time.ZonedDateTime
import java.util.UUID

import cats.syntax.either._
import io.circe._
import io.circe.syntax._
import org.specs2.mutable.Specification
import zendesk.model.value.{Tag, _}


class TicketSpec extends Specification {
  private val rawJson =
    """{
      |  "_id" : "436bf9b0-1147-4c0a-8439-6f79833bff5b",
      |  "url" : "http://initech.zendesk.com/api/v2/tickets/436bf9b0-1147-4c0a-8439-6f79833bff5b.json",
      |  "external_id" : "9210cdc9-4bee-485f-a078-35396cd74063",
      |  "created_at" : "2016-04-28T11:19:34 -10:00",
      |  "type" : "incident",
      |  "subject" : "A Catastrophe in Korea (North)",
      |  "description" : "Nostrud ad sit velit cupidatat laboris ipsum nisi amet laboris",
      |  "priority" : "high",
      |  "status" : "pending",
      |  "submitter_id" : 38,
      |  "assignee_id" : 24,
      |  "organization_id" : 116,
      |  "tags" : [
      |    "Ohio",
      |    "Pennsylvania"
      |  ],
      |  "has_incidents" : false,
      |  "due_at" : "2016-07-31T02:37:50 -10:00",
      |  "via" : "web"
      |}""".stripMargin
  private val tags = List(Tag("Ohio"), Tag("Pennsylvania"))
  private val expectedTicket = Ticket(
    id = TicketId(UUID.fromString("436bf9b0-1147-4c0a-8439-6f79833bff5b")),
    url = Url("http://initech.zendesk.com/api/v2/tickets/436bf9b0-1147-4c0a-8439-6f79833bff5b.json"),
    externalId = ExternalId(UUID.fromString("9210cdc9-4bee-485f-a078-35396cd74063")),
    createdAt = DateTime(ZonedDateTime.parse("2016-04-28T11:19:34-10:00")),
    `type` = Some(Incident),
    subject = Subject("A Catastrophe in Korea (North)"),
    description = Description("Nostrud ad sit velit cupidatat laboris ipsum nisi amet laboris"),
    priority = High,
    status = Pending,
    submitterId = SubmitterId(38),
    assigneeId = AssigneeId(24),
    organizationId = OrganizationId(116),
    tags = tags,
    hasIncidents = HasIncidents(false),
    dueAt = DateTime(ZonedDateTime.parse("2016-07-31T02:37:50-10:00")),
    via = Web
  )


  "JSON string for a organization should be decoded to Organization object" >> {
    val result: Either[Error, Ticket] = io.circe.parser.decode[Ticket](rawJson)

    "result should be expected Organization Object" >> {
      result must beEqualTo(expectedTicket.asRight)
    }
  }
  "A organization object should be encoded to Json String" >> {
    val result = expectedTicket.asJson.spaces2

    "result should be expected Organization Object" >> {
      result must beEqualTo(rawJson)
    }
  }
}
