package zendesk.util

import org.specs2.ScalaCheck
import org.specs2.mutable.Specification
import zendesk.helper.TestDataFactory
import zendesk.model
import zendesk.model._
import zendesk.model.value.{EmptyStringSearchField, Id, SubmitterId}

import scala.util.Random

class SearchDatabaseSpec extends Specification with ScalaCheck {
  "Build Search DB from the given Data" >> {
    val users = DataFileReader.getDataFromFile[User]("./data/users.json")
    val organizations = DataFileReader.getDataFromFile[Organization]("./data/organizations.json")
    val tickets = DataFileReader.getDataFromFile[Ticket]("./data/tickets.json")

    val db = SearchDatabase(userData = users, organizationData = organizations, ticketData = tickets)

    "Provides lists of searchable fields for each object" >> {
      "Users" >> {
        val result = db.listOfSearchableFields(Searchable.Users)

        result must beEqualTo(TestDataFactory.listOfSearchableFieldsForUsers)
      }

      "Tickets" >> {
        val result = db.listOfSearchableFields(Searchable.Tickets)

        result must beEqualTo(TestDataFactory.listOfSearchableFieldsForTickets)
      }

      "Organizations" >> {
        val result = db.listOfSearchableFields(Searchable.Organizations)

        result must beEqualTo(TestDataFactory.listOfSearchableFieldsForOrganizations)
      }
    }

    "Search Ticket" >> {
      val randomPickedUpTicket = Random.shuffle(tickets).head

      "should not be able to search with invalid 'submitterId'" >> {
        val result: Vector[Ticket] =
          db.query[Ticket](QueryParams(Searchable.Tickets, "submitterId", SubmitterId(99999999999L)))

        result must beEmpty
      }

      "should be able to search with 'id'" >> {
        val result: Vector[Ticket] = db.query[Ticket](QueryParams(Searchable.Tickets, "id", randomPickedUpTicket.id))

        result must contain(randomPickedUpTicket)
      }

      "should be able to search with 'url'" >> {
        val result: Vector[Ticket] =
          db.query[Ticket](model.QueryParams(Searchable.Tickets, "url", randomPickedUpTicket.url))
        result must contain(randomPickedUpTicket)
      }

      "should be able to search with 'externalId'" >> {
        val result: Vector[Ticket] =
          db.query[Ticket](model.QueryParams(Searchable.Tickets, "externalId", randomPickedUpTicket.externalId))
        result must contain(randomPickedUpTicket)
      }

      "should be able to search with 'createdAt'" >> {
        val result: Vector[Ticket] =
          db.query[Ticket](model.QueryParams(Searchable.Tickets, "createdAt", randomPickedUpTicket.createdAt))
        result must contain(randomPickedUpTicket)
      }

      "should be able to search with 'type'" >> {
        val result: Vector[Ticket] = db.query[Ticket](
          model.QueryParams(Searchable.Tickets, "type", randomPickedUpTicket.`type`.getOrElse(EmptyStringSearchField)))
        result must contain(randomPickedUpTicket)
      }

      "should be able to search with 'subject'" >> {
        val result: Vector[Ticket] =
          db.query[Ticket](model.QueryParams(Searchable.Tickets, "subject", randomPickedUpTicket.subject))
        result must contain(randomPickedUpTicket)
      }

      "should be able to search with 'description'" >> {
        val result: Vector[Ticket] =
          db.query[Ticket](
            model.QueryParams(
              Searchable.Tickets,
              "description",
              randomPickedUpTicket.description.getOrElse(EmptyStringSearchField)))
        result must contain(randomPickedUpTicket)
      }

      "should be able to search with 'priority'" >> {
        val result: Vector[Ticket] =
          db.query[Ticket](model.QueryParams(Searchable.Tickets, "priority", randomPickedUpTicket.priority))
        result must contain(randomPickedUpTicket)
      }

      "should be able to search with 'status'" >> {
        val result: Vector[Ticket] =
          db.query[Ticket](model.QueryParams(Searchable.Tickets, "status", randomPickedUpTicket.status))
        result must contain(randomPickedUpTicket)
      }

      "should be able to search with 'submitterId'" >> {
        val result: Vector[Ticket] =
          db.query[Ticket](model.QueryParams(Searchable.Tickets, "submitterId", randomPickedUpTicket.submitterId))
        result must contain(randomPickedUpTicket)
      }

      "should be able to search with 'assigneeId'" >> {
        val result: Vector[Ticket] =
          db.query[Ticket](
            model.QueryParams(
              Searchable.Tickets,
              "assigneeId",
              randomPickedUpTicket.assigneeId.getOrElse(EmptyStringSearchField)))
        result must contain(randomPickedUpTicket)
      }

      "should be able to search with 'organizationId'" >> {
        val result: Vector[Ticket] =
          db.query[Ticket](
            model.QueryParams(
              Searchable.Tickets,
              "organizationId",
              randomPickedUpTicket.organizationId.getOrElse(EmptyStringSearchField)))
        result must contain(randomPickedUpTicket)
      }

      "should be able to search with 'hasIncidents'" >> {
        val result: Vector[Ticket] =
          db.query[Ticket](model.QueryParams(Searchable.Tickets, "hasIncidents", randomPickedUpTicket.hasIncidents))
        result must contain(randomPickedUpTicket)
      }

      "should be able to search with 'dueAt'" >> {
        val result: Vector[Ticket] =
          db.query[Ticket](
            model
              .QueryParams(Searchable.Tickets, "dueAt", randomPickedUpTicket.dueAt.getOrElse(EmptyStringSearchField)))
        result must contain(randomPickedUpTicket)
      }

      "should be able to search with 'via'" >> {
        val result: Vector[Ticket] =
          db.query[Ticket](model.QueryParams(Searchable.Tickets, "via", randomPickedUpTicket.via))
        result must contain(randomPickedUpTicket)
      }

      "should be able to search with 'tags'" >> {
        randomPickedUpTicket.tags.map { tag =>
          val result: Vector[Ticket] =
            db.query[Ticket](model.QueryParams(Searchable.Tickets, "tags", tag))

          result must contain(randomPickedUpTicket)
        }
      }
    }

    "Search Organization" >> {
      val randomPickedUpOrg = Random.shuffle(organizations).head

      "should not be able to search with invalid 'id'" >> {
        val result: Vector[Organization] =
          db.query[Organization](QueryParams(Searchable.Organizations, "id", Id(99999999999L)))

        result must beEmpty
      }

      "should be able to search with 'id'" >> {
        val result: Vector[Organization] =
          db.query[Organization](model.QueryParams(Searchable.Organizations, "id", randomPickedUpOrg.id))

        result must contain(randomPickedUpOrg)
      }

      "should be able to search with 'url'" >> {
        val result: Vector[Organization] =
          db.query[Organization](model.QueryParams(Searchable.Organizations, "url", randomPickedUpOrg.url))
        result must contain(randomPickedUpOrg)
      }

      "should be able to search with 'externalId'" >> {
        val result: Vector[Organization] = db.query[Organization](
          model.QueryParams(Searchable.Organizations, "externalId", randomPickedUpOrg.externalId))
        result must contain(randomPickedUpOrg)
      }

      "should be able to search with 'name'" >> {
        val result: Vector[Organization] =
          db.query[Organization](model.QueryParams(Searchable.Organizations, "name", randomPickedUpOrg.name))
        result must contain(randomPickedUpOrg)
      }

      "should be able to search with 'domainName'" >> {
        randomPickedUpOrg.domainNames.map { domainName =>
          val result: Vector[Organization] =
            db.query[Organization](model.QueryParams(Searchable.Organizations, "domainNames", domainName))

          result must contain(randomPickedUpOrg)
        }
      }

      "should be able to search with 'tags'" >> {
        randomPickedUpOrg.tags.map { tag =>
          val result: Vector[Organization] =
            db.query[Organization](model.QueryParams(Searchable.Organizations, "tags", tag))

          result must contain(randomPickedUpOrg)
        }
      }

      "should be able to search with 'createdAt'" >> {
        val result: Vector[Organization] =
          db.query[Organization](model.QueryParams(Searchable.Organizations, "createdAt", randomPickedUpOrg.createdAt))
        result must contain(randomPickedUpOrg)
      }

      "should be able to search with 'details'" >> {
        val result: Vector[Organization] =
          db.query[Organization](model.QueryParams(Searchable.Organizations, "details", randomPickedUpOrg.details))
        result must contain(randomPickedUpOrg)
      }

      "should be able to search with 'sharedTickets'" >> {
        val result: Vector[Organization] =
          db.query[Organization](
            model.QueryParams(Searchable.Organizations, "sharedTickets", randomPickedUpOrg.sharedTickets))
        result must contain(randomPickedUpOrg)
      }
    }

    "Search Users" >> {
      val randomPickedUpUser = Random.shuffle(users).head

      "should not be able to search with invalid 'id'" >> {
        val result: Vector[User] =
          db.query[User](QueryParams(Searchable.Users, "id", Id(99999999999L)))

        result must beEmpty
      }

      "should be able to search with 'id'" >> {
        val result: Vector[User] = db.query[User](model.QueryParams(Searchable.Users, "id", randomPickedUpUser.id))

        result must contain(randomPickedUpUser)
      }

      "should be able to search with 'url'" >> {
        val result: Vector[User] = db.query[User](model.QueryParams(Searchable.Users, "url", randomPickedUpUser.url))
        result must contain(randomPickedUpUser)
      }

      "should be able to search with 'externalId'" >> {
        val result: Vector[User] =
          db.query[User](model.QueryParams(Searchable.Users, "externalId", randomPickedUpUser.externalId))
        result must contain(randomPickedUpUser)
      }

      "should be able to search with 'name'" >> {
        val result: Vector[User] = db.query[User](model.QueryParams(Searchable.Users, "name", randomPickedUpUser.name))
        result must contain(randomPickedUpUser)
      }

      "should be able to search with 'alias'" >> {
        val result: Vector[User] =
          db.query[User](
            model.QueryParams(Searchable.Users, "alias", randomPickedUpUser.alias.getOrElse(EmptyStringSearchField)))
        result must contain(randomPickedUpUser)
      }

      "should be able to search with 'createdAt'" >> {
        val result: Vector[User] =
          db.query[User](model.QueryParams(Searchable.Users, "createdAt", randomPickedUpUser.createdAt))
        result must contain(randomPickedUpUser)
      }

      "should be able to search with 'active'" >> {
        val result: Vector[User] =
          db.query[User](model.QueryParams(Searchable.Users, "active", randomPickedUpUser.active))
        result must contain(randomPickedUpUser)
      }

      "should be able to search with 'verified'" >> {
        val result: Vector[User] =
          db.query[User](
            model
              .QueryParams(Searchable.Users, "verified", randomPickedUpUser.verified.getOrElse(EmptyStringSearchField)))
        result must contain(randomPickedUpUser)
      }

      "should be able to search with 'shared'" >> {
        val result: Vector[User] =
          db.query[User](model.QueryParams(Searchable.Users, "shared", randomPickedUpUser.shared))
        result must contain(randomPickedUpUser)
      }

      "should be able to search with 'locale'" >> {
        val result: Vector[User] =
          db.query[User](
            model.QueryParams(Searchable.Users, "locale", randomPickedUpUser.locale.getOrElse(EmptyStringSearchField)))
        result must contain(randomPickedUpUser)
      }

      "should be able to search with 'timezone'" >> {
        val result: Vector[User] =
          db.query[User](
            model
              .QueryParams(Searchable.Users, "timezone", randomPickedUpUser.timezone.getOrElse(EmptyStringSearchField)))
        result must contain(randomPickedUpUser)
      }

      "should be able to search with 'lastLoginAt'" >> {
        val result: Vector[User] =
          db.query[User](model.QueryParams(Searchable.Users, "lastLoginAt", randomPickedUpUser.lastLoginAt))
        result must contain(randomPickedUpUser)
      }

      "should be able to search with 'email'" >> {
        val result: Vector[User] =
          db.query[User](
            model.QueryParams(Searchable.Users, "email", randomPickedUpUser.email.getOrElse(EmptyStringSearchField)))
        result must contain(randomPickedUpUser)
      }

      "should be able to search with 'phone'" >> {
        val result: Vector[User] =
          db.query[User](model.QueryParams(Searchable.Users, "phone", randomPickedUpUser.phone))
        result must contain(randomPickedUpUser)
      }

      "should be able to search with 'signature'" >> {
        val result: Vector[User] =
          db.query[User](model.QueryParams(Searchable.Users, "signature", randomPickedUpUser.signature))
        result must contain(randomPickedUpUser)
      }

      "should be able to search with 'organizationId'" >> {
        val result: Vector[User] =
          db.query[User](
            model.QueryParams(
              Searchable.Users,
              "organizationId",
              randomPickedUpUser.organizationId.getOrElse(EmptyStringSearchField)))
        result must contain(randomPickedUpUser)
      }

      "should be able to search with 'tags'" >> {
        randomPickedUpUser.tags.map { tag =>
          val result: Vector[User] =
            db.query[User](model.QueryParams(Searchable.Users, "tags", tag))

          result must contain(randomPickedUpUser)
        }
      }
    }
  }
}
