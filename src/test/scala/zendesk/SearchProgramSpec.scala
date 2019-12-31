package zendesk

import cats.Id
import org.specs2.mutable.{Spec, Specification}
import org.specs2.specification.BeforeEach
import zendesk.util.parser.SearchOptionCommand
import zendesk.util.parser.SearchOptionCommand.{Quit, SearchZendesk, ViewSearchableFields}
import cats.syntax.either._
import org.specs2.matcher.{EitherMatchers, ResultMatchers}
import zendesk.model.ParseFailure
import zendesk.util.MessageFactory

class SearchProgramSpec extends Specification with EitherMatchers with ResultMatchers with BeforeEach {
  sequential

  import zendesk.helper.IdInterpreters._

  val idProgram = new SearchProgram[Id]()

  "processSelectSearchOptions" >> {
    "When valid input is provided" >> {
      "When input is '1' it should parse as SearchOptionCommand" >> {
        IdConsole.dummyInput = Vector("1")

        val result: Id[Either[model.AppError, SearchOptionCommand]] = idProgram.processSelectSearchOptions()

        IdConsole.dummyOutput must beEqualTo(Vector(MessageFactory.searchOptions))
        result must beEqualTo(SearchZendesk.asRight)
      }

      "When input is '2' it should parse as SearchOptionCommand" >> {
        IdConsole.dummyInput = Vector("2")

        val result: Id[Either[model.AppError, SearchOptionCommand]] = idProgram.processSelectSearchOptions()

        IdConsole.dummyOutput must beEqualTo(Vector(MessageFactory.searchOptions))
        result must beEqualTo(ViewSearchableFields.asRight)
      }

      "When input is 'quit' it should parse as SearchOptionCommand" >> {
        IdConsole.dummyInput = Vector("quit")

        val result: Id[Either[model.AppError, SearchOptionCommand]] = idProgram.processSelectSearchOptions()

        IdConsole.dummyOutput must beEqualTo(Vector(MessageFactory.searchOptions))
        result must beEqualTo(Quit.asRight)
      }

      "When input is '4' it should parse as SearchOptionCommand" >> {
        IdConsole.dummyInput = Vector("4")

        val result: Id[Either[model.AppError, SearchOptionCommand]] = idProgram.processSelectSearchOptions()

        IdConsole.dummyOutput must beEqualTo(Vector(MessageFactory.searchOptions))
        result must beEqualTo(ParseFailure("Cannot parse '4' as SearchOptionCommand").asLeft)
      }
    }
  }

  override protected def before: Any = IdConsole.resetInputAndOutput()
}
