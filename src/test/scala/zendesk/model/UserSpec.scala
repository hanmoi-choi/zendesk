package zendesk.model

import java.util.UUID

import cats.syntax.either._
import io.circe._
import io.circe.syntax._
import org.joda.time.DateTime
import org.specs2.mutable.Specification
import zendesk.helper.SampleDataGen
import zendesk.model.value._

class UserSpec extends Specification {
  "JSON string for a user should be decoded to User object" >> {
    val result: Either[Error, User] = io.circe.parser.decode[User](SampleDataGen.rawUserJson)

    "result should be expected User Object" >> {
      result must beEqualTo(SampleDataGen.user.asRight)
    }
  }

  "A user object should be encoded to Json String" >> {
    val result = SampleDataGen.user.asJson.spaces2

    "result should be expected User Object" >> {
      result must beEqualTo(SampleDataGen.rawUserJson)
    }
  }
}