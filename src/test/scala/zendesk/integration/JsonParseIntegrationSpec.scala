package zendesk.integration

import java.nio.file.{Files, Path}

import io.circe
import org.specs2.mutable.Specification
import zendesk.model.User

import scala.io.Source
import io.circe.parser.decode

class JsonParseIntegrationSpec extends Specification {

  "JSON string for a users, data/users.json file" >> {
    val fileContents = Source.fromFile("./data/users.json").getLines.mkString
    val users: Either[circe.Error, List[User]] = decode[List[User]](fileContents)

    //❯ cat data/users.json | jq '. | length'
    //75
    "JSON file should be parsed without failure" >> {
      users must beRight()
      users.map(_.size) must be(Right(75))
    }
  }
}
