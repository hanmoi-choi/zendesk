package zendesk.service.parser

import cats.syntax.either._
import org.specs2.ScalaCheck
import org.specs2.mutable.Specification
import zendesk.model.ParseFailure
import zendesk.service.parser.ApplicationOptionCommand.{ApplicationZendesk, Quit, ViewSearchableFields}

class ApplicationOptionCommandParserSpec extends Specification with ScalaCheck {
  private val parser = ApplicationOptionCommandParser()

  "Parse Zendesk Search App ApplicationOptionCommand" >> {
    "Should parse '1' as 'SearchZendesk' command" >> {
      parser.doParse("1") must beEqualTo(ApplicationZendesk.asRight)
    }

    "Should parse '2' as 'ViewSearchableFields' command" >> {
      parser.doParse("2") must beEqualTo(ViewSearchableFields.asRight)
    }

    "Should parse 'quit' as 'Quit' command" >> {
      "as uppercase" >> {
        parser.doParse("QUIT") must beEqualTo(Quit.asRight)
      }

      "as lowercase" >> {
        parser.doParse("quit") must beEqualTo(Quit.asRight)
      }
    }

    "any other string inputs" >> prop { invalidCommand: String =>
      (invalidCommand != "1" && invalidCommand != "2" && invalidCommand.toLowerCase != "quit") ==> prop { _: String =>
        val result = parser.doParse(invalidCommand)
        val expectedError = ParseFailure(s"Cannot parse '$invalidCommand' as ApplicationOptionCommand").asLeft

        result must beEqualTo(expectedError)
      }
    }.set(minTestsOk = 50, workers = 3)

  }

}
