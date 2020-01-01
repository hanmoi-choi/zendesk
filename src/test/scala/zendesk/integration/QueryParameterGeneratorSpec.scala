package zendesk.integration

import java.util.UUID

import cats.syntax.either._
import org.specs2.ScalaCheck
import org.specs2.mutable.Specification
import zendesk.model.value.{Id, QueryParams, TicketId}
import zendesk.model.value.SearchObject.{Organization, Ticket, User}
import zendesk.service.QueryParameterGenerator
import zendesk.service.parser.SearchObjectCommand.{SearchOrganizations, SearchTickets, SearchUsers}
import zendesk.service.parser.{SearchOrganizationsTermParser, SearchTicketsTermParser, SearchUsersTermParser}

/*
 Comprehensive tests is covered at each `Parser` so this test will be make sure generator return expected value simply.
 */

class QueryParameterGeneratorSpec extends Specification with ScalaCheck {
  private val generator = QueryParameterGenerator(
    SearchUsersTermParser,
    SearchTicketsTermParser,
    SearchOrganizationsTermParser
  )

  "Query parameter to search users" >> {
    val result = generator.generate(SearchUsers, "id", "1")

    result must beEqualTo(QueryParams(User, "Id", Id(1)).asRight)
  }

  "Query parameter to search Tickets" >> {
    val uuid = UUID.randomUUID()
    val result = generator.generate(SearchTickets, "id", uuid.toString)

    result must beEqualTo(QueryParams(Ticket, "TicketId", TicketId(uuid)).asRight)
  }

  "Query parameter to search Organizations" >> {
    val result = generator.generate(SearchOrganizations, "id", "1")

    result must beEqualTo(QueryParams(Organization, "Id", Id(1)).asRight)
  }
}
