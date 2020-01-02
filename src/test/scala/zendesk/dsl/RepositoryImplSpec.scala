package zendesk.dsl

import java.util.UUID

import cats.syntax.either._
import org.specs2.mutable.Specification
import zendesk.dsl.Repository.queryImpl
import zendesk.helper.TestDataFactory
import zendesk.model.SearchResult.ForeignKey
import zendesk.model.{QueryParams, SearchResult, Searchable, Ticket, User}
import zendesk.model.value.{AssigneeId, Id, OrganizationId, SubmitterId, TicketId}
import zendesk.util.Database

class RepositoryImplSpec extends Specification {
  "queryImpl - Used for both Id and IO Monad; Don't want to copy and paste for testing" >> {
    "Query for Users" >> {
      "Should return relations of Tickets and Organization" >> {
        val user = TestDataFactory.user.copy(id = Id(1), organizationId = Some(OrganizationId(1)))
        val users = Vector(user)

        val orgForOrganizationId = TestDataFactory.organization.copy(id = Id(1))
        val orgs = Vector(
          orgForOrganizationId,
          TestDataFactory.organization.copy(id = Id(2)),
          TestDataFactory.organization.copy(id = Id(3))
        )

        val ticketForSubmitterId = TestDataFactory.ticket.copy(submitterId = SubmitterId(1))
        val ticketForAssigneeId = TestDataFactory.ticket.copy(assigneeId = Some(AssigneeId(1)))
        val tickets = Vector(
          ticketForSubmitterId,
          ticketForAssigneeId,
          TestDataFactory.ticket.copy(submitterId = SubmitterId(2)),
          TestDataFactory.ticket.copy(assigneeId = Some(AssigneeId(2)))
        )

        val DB = Database(users, orgs, tickets)
        val params = QueryParams(Searchable.Users, "Id", Id(1))
        val relations = Map[ForeignKey, Vector[Searchable]](
          SearchResult.SubmitterId -> Vector(ticketForSubmitterId),
          SearchResult.AssigneeId -> Vector(ticketForAssigneeId),
          SearchResult.OrganizationIdAtUser -> Vector(orgForOrganizationId)
        )

        val expected = Vector(SearchResult(params, user, relations))

        val result = queryImpl(params)(DB)

        result must beEqualTo(expected.asRight)
      }
    }

    "Query for Tickets" >> {
      "Should return relations of User and Organization" >> {
        val uuid = UUID.randomUUID()
        val userForSubmitterId = TestDataFactory.user.copy(id = Id(1))
        val userForAssigneeId = TestDataFactory.user.copy(id = Id(2))
        val users = Vector(
          userForAssigneeId,
          userForSubmitterId,
          TestDataFactory.user.copy(id = Id(3)),
          TestDataFactory.user.copy(id = Id(4))
        )

        val orgForOrganizationId = TestDataFactory.organization.copy(id = Id(1))
        val orgs = Vector(
          orgForOrganizationId,
          TestDataFactory.organization.copy(id = Id(2))
        )

        val ticket = TestDataFactory.ticket.copy(
          id = TicketId(uuid),
          submitterId = SubmitterId(1),
          assigneeId = Some(AssigneeId(2)),
          organizationId = Some(OrganizationId(1))
        )
        val tickets = Vector(ticket)

        val DB = Database(users, orgs, tickets)
        val params = QueryParams(Searchable.Tickets, "Id", TicketId(uuid))
        val relations = Map[ForeignKey, Vector[Searchable]](
          SearchResult.SubmitterId -> Vector(userForSubmitterId),
          SearchResult.AssigneeId -> Vector(userForAssigneeId),
          SearchResult.OrganizationIdAtTicket -> Vector(orgForOrganizationId)
        )

        val expected = Vector(SearchResult(params, ticket, relations))

        val result = queryImpl(params)(DB)

        result must beEqualTo(expected.asRight)
      }
    }

    "Query for Organizations" >> {
      "Should return relations of User and Tickets" >> {
        val users = Vector(TestDataFactory.user.copy(organizationId = Some(OrganizationId(1))))

        val org = TestDataFactory.organization.copy(id = Id(1))
        val orgs = Vector(org)

        val tickets = Vector(TestDataFactory.ticket.copy(organizationId = Some(OrganizationId(1))))

        val DB = Database(users, orgs, tickets)
        val params = QueryParams(Searchable.Organizations, "Id", Id(1))

        val relations = Map[ForeignKey, Vector[Searchable]](
          SearchResult.OrganizationIdAtUser -> users,
          SearchResult.OrganizationIdAtTicket -> tickets
        )
        val expected = Vector(SearchResult(params, org, relations))

        val result = queryImpl(params)(DB)

        result must beEqualTo(expected.asRight)
      }
    }
  }
}
