package zendesk.service

import java.util.UUID

import org.specs2.mutable.Specification
import zendesk.helper.TestDataFactory
import zendesk.model.SearchResult.ForeignKey
import zendesk.model.value.{Id, TicketId}
import zendesk.model.{QueryParams, SearchResult, Searchable}
class SearchResultFormatterTest extends Specification {
  private val user = TestDataFactory.user
  private val organization = TestDataFactory.organization
  private val ticket = TestDataFactory.ticket
  private val formatter = SearchResultFormatter()

  "SearchResultFormatter" should {
    "Should return human readable format" in {
      "Primary Object is Users" >> {
        val searchResultTitle =
          s"""
             |---------------------------------------------------
             | Search Results for this parameters
             |   - Object: Users
             |   - Term: Id
             |   - Value: 1
             |---------------------------------------------------
             |""".stripMargin

        val expectedSearchResultForPrimaryObject =
          s"""## Primary Object
             |${user.asFullDataString}
             |
             |## Relations
             |### Tickets as Submitter
             |${ticket.asSimpleDataString}
             |
             |### Tickets as Assignee
             |${ticket.asSimpleDataString}
             |
             |### Organizations
             |${organization.asSimpleDataString}
             |
             |
             |---------------------------------------------------""".stripMargin

        val queryParams = {
          QueryParams(Searchable.Users, "Id", Id(1))
        }
        val relations = Map[ForeignKey, Vector[Searchable]](
          SearchResult.SubmitterId -> Vector(ticket),
          SearchResult.AssigneeId -> Vector(ticket),
          SearchResult.OrganizationIdAtUser -> Vector(organization)
        )
        val searchResult = SearchResult(queryParams, user, relations)
        val expectedResultString = searchResultTitle + expectedSearchResultForPrimaryObject

        val result = formatter.format(Vector(searchResult))

        result must beEqualTo(expectedResultString)
      }
      "Primary Object is Tickets" >> {
        val searchResultTitle =
          s"""
             |---------------------------------------------------
             | Search Results for this parameters
             |   - Object: Tickets
             |   - Term: Id
             |   - Value: 436bf9b0-1147-4c0a-8439-6f79833bff5b
             |---------------------------------------------------
             |""".stripMargin

        val expectedSearchResultForPrimaryObject =
          s"""## Primary Object
             |${ticket.asFullDataString}
             |
             |## Relations
             |### Users as Submitter
             |${user.asSimpleDataString}
             |
             |### Users as Assignee
             |${user.asSimpleDataString}
             |
             |### Organizations
             |${organization.asSimpleDataString}
             |
             |
             |---------------------------------------------------""".stripMargin

        val queryParams = {
          QueryParams(Searchable.Tickets, "Id", TicketId(UUID.fromString("436bf9b0-1147-4c0a-8439-6f79833bff5b")))
        }
        val relations = Map[ForeignKey, Vector[Searchable]](
          SearchResult.SubmitterId -> Vector(user),
          SearchResult.AssigneeId -> Vector(user),
          SearchResult.OrganizationIdAtTicket -> Vector(organization)
        )
        val searchResult = SearchResult(queryParams, ticket, relations)
        val expectedResultString = searchResultTitle + expectedSearchResultForPrimaryObject

        val result = formatter.format(Vector(searchResult))

        result must beEqualTo(expectedResultString)
      }
      "Primary Object is Organizations" >> {
        val searchResultTitle =
          s"""
             |---------------------------------------------------
             | Search Results for this parameters
             |   - Object: Organizations
             |   - Term: Id
             |   - Value: 101
             |---------------------------------------------------
             |""".stripMargin

        val expectedSearchResultForPrimaryObject =
          s"""## Primary Object
             |${organization.asFullDataString}
             |
             |## Relations
             |### Organizations
             |${user.asSimpleDataString}
             |
             |### Organizations
             |${ticket.asSimpleDataString}
             |
             |
             |---------------------------------------------------""".stripMargin

        val queryParams = {
          QueryParams(Searchable.Organizations, "Id", Id(101))
        }
        val relations = Map[ForeignKey, Vector[Searchable]](
          SearchResult.OrganizationIdAtUser -> Vector(user),
          SearchResult.OrganizationIdAtTicket -> Vector(ticket)
        )
        val searchResult = SearchResult(queryParams, organization, relations)

        val expectedResultString = searchResultTitle + expectedSearchResultForPrimaryObject

        val result = formatter.format(Vector(searchResult))

        result must beEqualTo(expectedResultString)
      }
    }
  }
}
