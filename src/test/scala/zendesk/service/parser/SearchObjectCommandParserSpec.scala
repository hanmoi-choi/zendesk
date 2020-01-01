package zendesk.service.parser

import cats.syntax.either._
import org.specs2.ScalaCheck
import org.specs2.mutable.Specification
import zendesk.model.ParseFailure
import zendesk.service.parser.SearchObjectCommand.{Quit, SearchOrganizations, SearchTickets, SearchUsers}
import zendesk.service.parser.SearchObjectCommandParser.doParse

import scala.language.postfixOps

class SearchObjectCommandParserSpec extends Specification with ScalaCheck {

  "Parse Zendesk SearchObjectCommand" >> {
    "Should parse '1' as 'SearchUsers' command" >> {
      doParse("1") must beEqualTo(SearchUsers.asRight)
    }

    "Should parse '2' as 'SearchTickets' command" >> {
      doParse("2") must beEqualTo(SearchTickets.asRight)
    }

    "Should parse '3' as 'SearchOrganizations' command" >> {
      doParse("3") must beEqualTo(SearchOrganizations.asRight)
    }

    "Should parse 'quit' as 'Quit' command" >> {
      "as uppercase" >> {
        doParse("QUIT") must beEqualTo(Quit.asRight)
      }

      "as lowercase" >> {
        doParse("quit") must beEqualTo(Quit.asRight)
      }
    }

    "any other string inputs" >> prop { invalidCommand: String =>
      (invalidCommand != "1" && invalidCommand != "2" && invalidCommand != "3" && invalidCommand.toLowerCase != "quit") ==> prop { _: String =>
        val result = doParse(invalidCommand)
        val expectedError = ParseFailure(s"Cannot parse $invalidCommand as SearchObjectCommand").asLeft

        result must beEqualTo(expectedError)
      }
    }.set(minTestsOk = 50, workers = 3)

  }

}
