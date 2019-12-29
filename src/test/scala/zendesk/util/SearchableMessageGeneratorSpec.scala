package zendesk.util

import org.specs2.mutable.Specification
import zendesk.helper.SampleDataGen
import zendesk.model.{Organization, Ticket, User}

class SearchableMessageGeneratorSpec extends Specification{
  "forObject Returns list of fields with which the object could be searched." >> {
    "for User object"  >> {
      val title = "Search Users with"

      val result = SearchableMessageGenerator.forObject[User](SampleDataGen.user, title)
      val expectedNames =
        s"""
          |-------------------------
          |${title}
          |-------------------------
          |id
          |url
          |externalId
          |name
          |alias
          |createdAt
          |active
          |verified
          |shared
          |locale
          |timezone
          |lastLoginAt
          |email
          |phone
          |signature
          |organizationId
          |tags
          |suspended
          |role
          |
          |""".stripMargin

      result must beEqualTo(expectedNames)
    }
    "for Ticket object"  >> {
      val title = "Search Tickets with"

      val result = SearchableMessageGenerator.forObject[Ticket](SampleDataGen.ticket, title)
      val expectedNames =
        s"""
           |-------------------------
           |${title}
           |-------------------------
           |id
           |url
           |externalId
           |createdAt
           |type
           |subject
           |description
           |priority
           |status
           |submitterId
           |assigneeId
           |organizationId
           |tags
           |hasIncidents
           |dueAt
           |via
           |
           |""".stripMargin

      result must beEqualTo(expectedNames)
    }
    "for Organization object"  >> {
      val title = "Search Organizations with"

      val result = SearchableMessageGenerator.forObject[Organization](SampleDataGen.organization, title)
      val expectedNames =
        s"""
           |-------------------------
           |${title}
           |-------------------------
           |id
           |url
           |externalId
           |name
           |domainNames
           |createdAt
           |details
           |sharedTickets
           |tags
           |
           |""".stripMargin

      result must beEqualTo(expectedNames)
    }
  }
}
