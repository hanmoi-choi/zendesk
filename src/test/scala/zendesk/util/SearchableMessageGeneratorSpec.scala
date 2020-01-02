package zendesk.util

import org.specs2.mutable.Specification
import zendesk.helper.TestDataFactory
import zendesk.model.{Organization, Ticket, User}

class SearchableMessageGeneratorSpec extends Specification {
  "forObject Returns list of fields with which the object could be searched." >> {
    "for User object" >> {
      val result = SearchableMessageGenerator.forObject[User](TestDataFactory.user)

      result must beEqualTo(TestDataFactory.listOfSearchableFieldsForUsers)
    }
    "for Ticket object" >> {
      val result = SearchableMessageGenerator.forObject[Ticket](TestDataFactory.ticket)

      result must beEqualTo(TestDataFactory.listOfSearchableFieldsForTickets)
    }
    "for Organization object" >> {
      val result = SearchableMessageGenerator.forObject[Organization](TestDataFactory.organization)

      result must beEqualTo(TestDataFactory.listOfSearchableFieldsForOrganizations)
    }
  }
}
