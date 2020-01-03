package zendesk.model

import cats.syntax.either._
import io.circe._
import io.circe.syntax._
import org.specs2.mutable.Specification
import zendesk.helper.TestDataFactory

class UserSpec extends Specification {
  "Simple Data String" >> {
    val expected =
      """
        |  user_name : Francisca Rasmussen
        |  alias : Miss Coffey
        |  user_role : admin""".stripMargin

    TestDataFactory.user.asSimpleDataString must beEqualTo(expected)
  }

  "JSON string for a user should be decoded to User object" >> {
    val result: Either[Error, User] = io.circe.parser.decode[User](TestDataFactory.rawUserJson)

    "result should be expected User Object" >> {
      result must beEqualTo(TestDataFactory.user.asRight)
    }
  }

  "A user object should be encoded to Json String" >> {
    val result = TestDataFactory.user.asJson.spaces2

    "result should be expected User Object" >> {
      result must beEqualTo(TestDataFactory.rawUserJson)
    }
  }
}
