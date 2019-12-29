package zendesk.model

import java.util.UUID

import cats.syntax.either._
import io.circe._
import io.circe.syntax._
import org.joda.time.DateTime
import org.specs2.mutable.Specification
import zendesk.helper.SampleDataGen
import zendesk.model.value.{Tag, _}


class TicketSpec extends Specification {


  "JSON string for a ticket should be decoded to Ticket object" >> {
    val result: Either[Error, Ticket] = io.circe.parser.decode[Ticket](SampleDataGen.rawTicketJson)

    "result should be expected Ticket Object" >> {
      result must beEqualTo(SampleDataGen.ticket.asRight)
    }
  }
  "A ticket object should be encoded to Json String" >> {
    val result = SampleDataGen.ticket.asJson.spaces2

    "result should be expected Ticket Object" >> {
      result must beEqualTo(SampleDataGen.rawTicketJson)
    }
  }
}
