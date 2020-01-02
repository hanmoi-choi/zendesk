package zendesk.service.parser

import cats.syntax.either._
import org.specs2.ScalaCheck
import org.specs2.mutable.Specification
import zendesk.model.{ExitAppByUserRequest, ParseFailure}
import zendesk.service.parser.SearchObjectCommand.{SearchOrganizations, SearchTickets, SearchUsers}

class SearchObjectCommandParserSpec extends Specification with ScalaCheck {
  private val parser = SearchObjectCommandParser()

  "Parse SearchObjectCommand" >> {
    "Should parse '1' as 'SearchUsers' command" >> {
      parser.doParse("1") must beEqualTo(SearchUsers.asRight)
    }

    "Should parse '2' as 'SearchTickets' command" >> {
      parser.doParse("2") must beEqualTo(SearchTickets.asRight)
    }

    "Should parse '3' as 'SearchOrganizations' command" >> {
      parser.doParse("3") must beEqualTo(SearchOrganizations.asRight)
    }

    "Should parse 'quit' as 'Quit' command" >> {
      "as uppercase" >> {
        parser.doParse("QUIT") must beEqualTo(ExitAppByUserRequest.asLeft)
      }

      "as lowercase" >> {
        parser.doParse("quit") must beEqualTo(ExitAppByUserRequest.asLeft)
      }
    }

    "any other string inputs" >> prop { invalidCommand: String =>
      (invalidCommand != "1" && invalidCommand != "2" && invalidCommand != "3" && invalidCommand.toLowerCase != "quit") ==> prop {
        _: String =>
          val result = parser.doParse(invalidCommand)
          val expectedError = ParseFailure(s"Cannot parse '$invalidCommand' as SearchObjectCommand").asLeft

          result must beEqualTo(expectedError)
      }
    }.set(minTestsOk = 50, workers = 3)

  }

}
