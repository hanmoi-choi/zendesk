package zendesk.integration

import java.nio.file.{Files, Path}

import io.circe
import org.specs2.mutable.Specification
import zendesk.model.{Organization, User}

import scala.io.Source
import io.circe.parser.decode

class JsonParseIntegrationSpec extends Specification {

  "JSON string for users, data/users.json file" >> {
    val fileContents = Source.fromFile("./data/users.json").getLines.mkString
    val users: Either[circe.Error, List[User]] = decode[List[User]](fileContents)

    // ❯ cat data/users.json | jq '. | length'
    // 75
    "JSON file should be parsed without failure" >> {
      users must beRight()
      users.map(_.size) must beRight(75)
    }
  }

  "JSON string for organizations, data/organizations.json file" >> {
    val fileContents = Source.fromFile("./data/organizations.json").getLines.mkString
    val users: Either[circe.Error, List[Organization]] = decode[List[Organization]](fileContents)

    // ❯ cat data/organizations.json | jq '. | length'
    // 25
    "JSON file should be parsed without failure" >> {
      users must beRight()
      users.map(_.size) must beRight(25)
    }
  }
}
