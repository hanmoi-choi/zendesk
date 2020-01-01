package zendesk.integration

import cats.syntax.either._
import org.specs2.ScalaCheck
import org.specs2.mutable.Specification
import zendesk.model.value.{Id, QueryParams}
import zendesk.model.value.SearchObject.{Ticket, User}
import zendesk.service.QueryParameterGenerator
import zendesk.service.parser.SearchObjectCommand.{SearchTickets, SearchUsers}
import zendesk.service.parser.{SearchOrganizationsTermParser, SearchTicketsTermParser, SearchUsersTermParser}

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
    val result = generator.generate(SearchTickets, "id", "1")

    result must beEqualTo(QueryParams(Ticket, "Id", Id(1)).asRight)
  }
}
