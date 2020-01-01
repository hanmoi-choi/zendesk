package zendesk.integration

import org.specs2.mutable.Specification
import zendesk.model.Organization.decodeOrg
import zendesk.model.Ticket.decodeTicket
import zendesk.model.User.decodeUser
import zendesk.model.{Organization, Ticket, User}
import zendesk.util.DataFileReader

class JsonParseIntegrationSpec extends Specification {
  "JSON string for users, data/users.json file" >> {
    val users = DataFileReader.getDataFromFile[User]("./data/users.json")

    // ❯ cat data/users.json | jq '. | length'
    // 75
    "JSON file should be parsed without failure" >> {
      users.size must beEqualTo(75)
    }
  }

  "JSON string for organizations, data/organizations.json file" >> {
    val orgs = DataFileReader.getDataFromFile[Organization]("./data/organizations.json")

    // ❯ cat data/organizations.json | jq '. | length'
    // 25
    "JSON file should be parsed without failure" >> {
      orgs.size must beEqualTo(25)
    }
  }

  "JSON string for tickets, data/tickets.json file" >> {
    val tickets = DataFileReader.getDataFromFile[Ticket]("./data/tickets.json")

    // ❯ cat data/tickets.json | jq '. | length'
    // 200
    "JSON file should be parsed without failure" >> {
      tickets.size must beEqualTo(200)
    }
  }
}
