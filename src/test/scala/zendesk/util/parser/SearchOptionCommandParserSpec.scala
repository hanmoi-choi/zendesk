package zendesk.util.parser

import cats.syntax.either._
import org.specs2.ScalaCheck
import org.specs2.mutable.Specification
import zendesk.model.ParseFailure
import zendesk.util.parser.SearchOptionCommand.{Quit, SearchZendesk, ViewSearchableFields}
import zendesk.util.parser.SearchOptionCommandParser.doParse

import scala.language.postfixOps

class SearchOptionCommandParserSpec extends Specification with ScalaCheck{

  "Parse Zendesk SearchOptionCommand" >> {
    "Should parse '1' as 'SearchZendesk' command" >> {
      doParse("1") must beEqualTo(SearchZendesk.asRight)
    }

    "Should parse '2' as 'ViewSearchableFields' command" >> {
      doParse("2") must beEqualTo(ViewSearchableFields.asRight)
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
      (invalidCommand != "1" && invalidCommand != "2" && invalidCommand.toLowerCase != "quit") ==> prop { _: String =>
        val result = doParse(invalidCommand)
        val expectedError = ParseFailure(s"Cannot parse $invalidCommand as SearchOptionCommand").asLeft

        result must beEqualTo(expectedError)
      }
    }.set(minTestsOk = 50, workers = 3)

  }

}
