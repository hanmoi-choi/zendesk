package zendesk.model

import cats.syntax.either._
import io.circe._
import io.circe.syntax._
import org.specs2.mutable.Specification
import zendesk.helper.TestDataFactory

class TicketSpec extends Specification {
  "Simple Data String" >> {
    val expected =
      """
        |  ticket_subject : A Catastrophe in Korea (North)
        |  ticket_priority : high
        |  ticket_status : pending""".stripMargin

    TestDataFactory.ticket.asSimpleDataString must beEqualTo(expected)
  }

  "JSON string for a ticket should be decoded to Ticket object" >> {
    val result: Either[Error, Ticket] = io.circe.parser.decode[Ticket](TestDataFactory.rawTicketJson)

    "result should be expected Ticket Object" >> {
      result must beEqualTo(TestDataFactory.ticket.asRight)
    }
  }
  "A ticket object should be encoded to Json String" >> {
    val result = TestDataFactory.ticket.asJson.spaces2

    "result should be expected Ticket Object" >> {
      result must beEqualTo(TestDataFactory.rawTicketJson)
    }
  }
}
