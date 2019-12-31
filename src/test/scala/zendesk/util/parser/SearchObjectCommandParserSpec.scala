package zendesk.util.parser

import cats.syntax.either._
import org.specs2.ScalaCheck
import org.specs2.mutable.Specification
import zendesk.model.ParseFailure
import zendesk.util.parser.SearchObjectCommandParser.doParse
import zendesk.util.parser.SearchObjectCommand.{Quit, SearchOrganizations, SearchTickets, SearchUsers}

import scala.language.postfixOps

class SearchObjectCommandParserSpec extends Specification with ScalaCheck{

  "Parse Zendesk SearchOptionCommand" >> {
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

    "any other string inputs" >> prop { command: String =>
      (command != "1" && command != "2" && command != "3" && command.toLowerCase != "quit") ==> prop { invalidCommand: String =>
        val result = doParse(invalidCommand)
        val expectedError = ParseFailure(s"Cannot parse $invalidCommand as SearchTermCommand").asLeft
        
        result must beEqualTo(expectedError)
      }
    }.set(minTestsOk = 50, workers = 3)

  }

}
