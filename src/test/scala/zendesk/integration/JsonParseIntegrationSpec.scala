package zendesk.integration

import io.circe
import io.circe.parser.decode
import org.specs2.mutable.Specification
import zendesk.model.{Organization, Ticket, User}

import scala.io.Source

class JsonParseIntegrationSpec extends Specification {
  private def readFileAsString(path: String) = {
    val fileContents = Source.fromFile(path)
    val rawJson = fileContents.mkString
    fileContents.close()

    rawJson
  }
  "JSON string for users, data/users.json file" >> {
    val rawJson = readFileAsString("./data/users.json")
    val users: Either[circe.Error, List[User]] = decode[List[User]](rawJson)

    // ❯ cat data/users.json | jq '. | length'
    // 75
    "JSON file should be parsed without failure" >> {
      users must beRight()
      users.map(_.size) must beRight(75)
    }
  }

  "JSON string for organizations, data/organizations.json file" >> {
    val rawJson = readFileAsString("./data/organizations.json")
    val users: Either[circe.Error, List[Organization]] = decode[List[Organization]](rawJson)

    // ❯ cat data/organizations.json | jq '. | length'
    // 25
    "JSON file should be parsed without failure" >> {
      users must beRight()
      users.map(_.size) must beRight(25)
    }
  }

  "JSON string for tickets, data/tickets.json file" >> {
    val rawJson = readFileAsString("./data/tickets.json")
    val tickets: Either[circe.Error, List[Ticket]] = decode[List[Ticket]](rawJson)

    // ❯ cat data/tickets.json | jq '. | length'
    // 200
    "JSON file should be parsed without failure" >> {
      tickets must beRight()
      tickets.map(_.size) must beRight(200)
    }
  }
}
