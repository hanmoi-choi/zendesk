package zendesk.util

import org.specs2.ScalaCheck
import org.specs2.mutable.Specification
import zendesk.helper.DataWithFileGen
import zendesk.model.{Organization, Ticket, User}
import zendesk.model.value.{EmptyStringSearchField, QueryParams, SearchObject}

import scala.util.Random

class SearchDatabaseSpec extends Specification with ScalaCheck {
  "Build Search DB from the given Data" >> {
    val users = DataWithFileGen.getDataFromFile[User]("./data/users.json")
    val orgs = DataWithFileGen.getDataFromFile[Organization]("./data/organizations.json")
    val tickets = DataWithFileGen.getDataFromFile[Ticket]("./data/tickets.json")

    val db = SearchDatabase(userData = users, organizationData = orgs, ticketData = tickets)

    "Search Ticket" >> {
      val randomPickedUpTicket = Random.shuffle(tickets).head

      "should be able to search with 'id'" >> {
        val result: Vector[Ticket] = db.query[Ticket](QueryParams(SearchObject.Ticket, "id", randomPickedUpTicket.id))

        result must contain(randomPickedUpTicket)
      }

      "should be able to search with 'url'" >> {
        val result: Vector[Ticket] = db.query[Ticket](QueryParams(SearchObject.Ticket, "url", randomPickedUpTicket.url))
        result must contain(randomPickedUpTicket)
      }

      "should be able to search with 'externalId'" >> {
        val result: Vector[Ticket] = db.query[Ticket](QueryParams(SearchObject.Ticket, "externalId", randomPickedUpTicket.externalId))
        result must contain(randomPickedUpTicket)
      }

      "should be able to search with 'createdAt'" >> {
        val result: Vector[Ticket] =
          db.query[Ticket](QueryParams(SearchObject.Ticket, "createdAt", randomPickedUpTicket.createdAt))
        result must contain(randomPickedUpTicket)
      }


      "should be able to search with 'type'" >> {
        val result: Vector[Ticket] = db.query[Ticket](QueryParams(SearchObject.Ticket, "type", randomPickedUpTicket.`type`.getOrElse(EmptyStringSearchField)))
        result must contain(randomPickedUpTicket)
      }

      //      "should be able to search with 'domainName'" >> {
      //        val result: Vector[Ticket] = db.query[Ticket](QueryParams(SearchObject.Ticket, "domainName", randomPickedUpTicket.domainNames))
      //        result must contain(randomPickedUpTicket)
      //      }




      "should be able to search with 'subject'" >> {
        val result: Vector[Ticket] =
          db.query[Ticket](QueryParams(SearchObject.Ticket, "subject", randomPickedUpTicket.subject))
        result must contain(randomPickedUpTicket)
      }

      "should be able to search with 'description'" >> {
        val result: Vector[Ticket] =
          db.query[Ticket](QueryParams(SearchObject.Ticket, "description", randomPickedUpTicket.description.getOrElse(EmptyStringSearchField)))
        result must contain(randomPickedUpTicket)
      }

      "should be able to search with 'priority'" >> {
        val result: Vector[Ticket] =
          db.query[Ticket](QueryParams(SearchObject.Ticket, "priority", randomPickedUpTicket.priority))
        result must contain(randomPickedUpTicket)
      }

      "should be able to search with 'status'" >> {
        val result: Vector[Ticket] =
          db.query[Ticket](QueryParams(SearchObject.Ticket, "status", randomPickedUpTicket.status))
        result must contain(randomPickedUpTicket)
      }

      "should be able to search with 'submitterId'" >> {
        val result: Vector[Ticket] =
          db.query[Ticket](QueryParams(SearchObject.Ticket, "submitterId", randomPickedUpTicket.submitterId))
        result must contain(randomPickedUpTicket)
      }

      "should be able to search with 'assigneeId'" >> {
        val result: Vector[Ticket] =
          db.query[Ticket](QueryParams(SearchObject.Ticket, "assigneeId", randomPickedUpTicket.assigneeId.getOrElse(EmptyStringSearchField)))
        result must contain(randomPickedUpTicket)
      }

      "should be able to search with 'organizationId'" >> {
        val result: Vector[Ticket] =
          db.query[Ticket](QueryParams(SearchObject.Ticket, "organizationId", randomPickedUpTicket.organizationId.getOrElse(EmptyStringSearchField)))
        result must contain(randomPickedUpTicket)
      }


      "should be able to search with 'hasIncidents'" >> {
        val result: Vector[Ticket] =
          db.query[Ticket](QueryParams(SearchObject.Ticket, "hasIncidents", randomPickedUpTicket.hasIncidents))
        result must contain(randomPickedUpTicket)
      }

      "should be able to search with 'dueAt'" >> {
        val result: Vector[Ticket] =
          db.query[Ticket](QueryParams(SearchObject.Ticket, "dueAt", randomPickedUpTicket.dueAt.getOrElse(EmptyStringSearchField)))
        result must contain(randomPickedUpTicket)
      }

      "should be able to search with 'via'" >> {
        val result: Vector[Ticket] =
          db.query[Ticket](QueryParams(SearchObject.Ticket, "via", randomPickedUpTicket.via))
        result must contain(randomPickedUpTicket)
      }

      //      "should be able to search with 'tags'" >> {
      //        val result: Vector[Ticket] = db.query[Ticket](QueryParams(SearchObject.Ticket, "tags", randomPickedUpTicket.tags))
      //        result must contain(randomPickedUpTicket)
      //      }
    }

    "Search Organization" >> {
      val randomPickedUpOrg = Random.shuffle(orgs).head

      "should be able to search with 'id'" >> {
        val result: Vector[Organization] = db.query[Organization](QueryParams(SearchObject.Organization, "id", randomPickedUpOrg.id))

        result must contain(randomPickedUpOrg)
      }

      "should be able to search with 'url'" >> {
        val result: Vector[Organization] = db.query[Organization](QueryParams(SearchObject.Organization, "url", randomPickedUpOrg.url))
        result must contain(randomPickedUpOrg)
      }

      "should be able to search with 'externalId'" >> {
        val result: Vector[Organization] = db.query[Organization](QueryParams(SearchObject.Organization, "externalId", randomPickedUpOrg.externalId))
        result must contain(randomPickedUpOrg)
      }

      "should be able to search with 'name'" >> {
        val result: Vector[Organization] = db.query[Organization](QueryParams(SearchObject.Organization, "name", randomPickedUpOrg.name))
        result must contain(randomPickedUpOrg)
      }

//      "should be able to search with 'domainName'" >> {
//        val result: Vector[Organization] = db.query[Organization](QueryParams(SearchObject.Organization, "domainName", randomPickedUpOrg.domainNames))
//        result must contain(randomPickedUpOrg)
//      }

      //      "should be able to search with 'tags'" >> {
      //        val result: Vector[Organization] = db.query[Organization](QueryParams(SearchObject.Organization, "tags", randomPickedUpOrg.tags))
      //        result must contain(randomPickedUpOrg)
      //      }

      "should be able to search with 'createdAt'" >> {
        val result: Vector[Organization] =
          db.query[Organization](QueryParams(SearchObject.Organization, "createdAt", randomPickedUpOrg.createdAt))
        result must contain(randomPickedUpOrg)
      }

      "should be able to search with 'details'" >> {
        val result: Vector[Organization] =
          db.query[Organization](QueryParams(SearchObject.Organization, "details", randomPickedUpOrg.details))
        result must contain(randomPickedUpOrg)
      }

      "should be able to search with 'sharedTickets'" >> {
        val result: Vector[Organization] =
          db.query[Organization](QueryParams(SearchObject.Organization, "sharedTickets", randomPickedUpOrg.sharedTickets))
        result must contain(randomPickedUpOrg)
      }


    }

    "Search Users" >> {
      val randomPickedUpUser = Random.shuffle(users).head

      "should be able to search with 'id'" >> {
        val result: Vector[User] = db.query[User](QueryParams(SearchObject.User, "id", randomPickedUpUser.id))

        result must contain(randomPickedUpUser)
      }

      "should be able to search with 'url'" >> {
        val result: Vector[User] = db.query[User](QueryParams(SearchObject.User, "url", randomPickedUpUser.url))
        result must contain(randomPickedUpUser)
      }

      "should be able to search with 'externalId'" >> {
        val result: Vector[User] = db.query[User](QueryParams(SearchObject.User, "externalId", randomPickedUpUser.externalId))
        result must contain(randomPickedUpUser)
      }

      "should be able to search with 'name'" >> {
        val result: Vector[User] = db.query[User](QueryParams(SearchObject.User, "name", randomPickedUpUser.name))
        result must contain(randomPickedUpUser)
      }

      "should be able to search with 'alias'" >> {
        val result: Vector[User] =
          db.query[User](QueryParams(SearchObject.User, "alias", randomPickedUpUser.alias.getOrElse(EmptyStringSearchField)))
        result must contain(randomPickedUpUser)
      }

      "should be able to search with 'createdAt'" >> {
        val result: Vector[User] =
          db.query[User](QueryParams(SearchObject.User, "createdAt", randomPickedUpUser.createdAt))
        result must contain(randomPickedUpUser)
      }

      "should be able to search with 'active'" >> {
        val result: Vector[User] =
          db.query[User](QueryParams(SearchObject.User, "active", randomPickedUpUser.active))
        result must contain(randomPickedUpUser)
      }

      "should be able to search with 'verified'" >> {
        val result: Vector[User] =
          db.query[User](QueryParams(SearchObject.User, "verified", randomPickedUpUser.verified.getOrElse(EmptyStringSearchField)))
        result must contain(randomPickedUpUser)
      }

      "should be able to search with 'shared'" >> {
        val result: Vector[User] =
          db.query[User](QueryParams(SearchObject.User, "shared", randomPickedUpUser.shared))
        result must contain(randomPickedUpUser)
      }

      "should be able to search with 'locale'" >> {
        val result: Vector[User] =
          db.query[User](QueryParams(SearchObject.User, "locale", randomPickedUpUser.locale.getOrElse(EmptyStringSearchField)))
        result must contain(randomPickedUpUser)
      }

      "should be able to search with 'timezone'" >> {
        val result: Vector[User] =
          db.query[User](QueryParams(SearchObject.User, "timezone", randomPickedUpUser.timezone.getOrElse(EmptyStringSearchField)))
        result must contain(randomPickedUpUser)
      }

      "should be able to search with 'lastLoginAt'" >> {
        val result: Vector[User] =
          db.query[User](QueryParams(SearchObject.User, "lastLoginAt", randomPickedUpUser.lastLoginAt))
        result must contain(randomPickedUpUser)
      }

      "should be able to search with 'email'" >> {
        val result: Vector[User] =
          db.query[User](QueryParams(SearchObject.User, "email", randomPickedUpUser.email.getOrElse(EmptyStringSearchField)))
        result must contain(randomPickedUpUser)
      }

      "should be able to search with 'phone'" >> {
        val result: Vector[User] =
          db.query[User](QueryParams(SearchObject.User, "phone", randomPickedUpUser.phone))
        result must contain(randomPickedUpUser)
      }

      "should be able to search with 'signature'" >> {
        val result: Vector[User] =
          db.query[User](QueryParams(SearchObject.User, "signature", randomPickedUpUser.signature))
        result must contain(randomPickedUpUser)
      }

      "should be able to search with 'organizationId'" >> {
        val result: Vector[User] =
          db.query[User](QueryParams(SearchObject.User, "organizationId", randomPickedUpUser.organizationId.getOrElse(EmptyStringSearchField)))
        result must contain(randomPickedUpUser)
      }

//      "should be able to search with 'tags'" >> {
//        val result: Vector[User] =
//          db.query[User](QueryParams(SearchObject.User, "tags", randomPickedUpUser.tags.headOption.orNull))
//
//        result must contain(randomPickedUpUser)
//      }
    }

    //    userTable.put("signature", data.groupBy(_.signature))
    //    userTable.put("organizationId", data.groupBy(_.organizationId.getOrElse(EmptyStringSearchField)))

  }
}
