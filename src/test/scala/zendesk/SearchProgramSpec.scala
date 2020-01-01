package zendesk

import cats.Id
import org.specs2.mutable.{Spec, Specification}
import org.specs2.specification.BeforeEach
import zendesk.service.parser.{ApplicationOptionCommand, SearchObjectCommand}
import zendesk.service.parser.ApplicationOptionCommand.{ApplicationZendesk, Quit, ViewSearchableFields}
import cats.syntax.either._
import org.specs2.matcher.{EitherMatchers, ResultMatchers}
import zendesk.model.ParseFailure
import zendesk.util.MessageFactory
import zendesk.service.parser.SearchObjectCommand.{SearchOrganizations, SearchTickets, SearchUsers}

class SearchProgramSpec extends Specification with EitherMatchers with ResultMatchers with BeforeEach {
  sequential

  import zendesk.helper.IdInterpreters._

  val idProgram = new SearchProgram[Id]()

  override protected def before: Any = IdConsole.resetInputAndOutput()

  "processSelectApplicationOptions" >> {
    "When valid input is provided" >> {
      "When input is '1' it should parse as ApplicationZendesk" >> {
        IdConsole.dummyInput = Vector("1")

        val result: Id[Either[model.AppError, ApplicationOptionCommand]] = idProgram.processSelectApplicationOptions()

        IdConsole.dummyOutput must beEqualTo(Vector(MessageFactory.appOptionsMessage))
        result must beEqualTo(ApplicationZendesk.asRight)
      }

      "When input is '2' it should parse as ViewSearchableFields" >> {
        IdConsole.dummyInput = Vector("2")

        val result: Id[Either[model.AppError, ApplicationOptionCommand]] = idProgram.processSelectApplicationOptions()

        IdConsole.dummyOutput must beEqualTo(Vector(MessageFactory.appOptionsMessage))
        result must beEqualTo(ViewSearchableFields.asRight)
      }

      "When input is 'quit' it should parse as Quit" >> {
        IdConsole.dummyInput = Vector("quit")

        val result: Id[Either[model.AppError, ApplicationOptionCommand]] = idProgram.processSelectApplicationOptions()

        IdConsole.dummyOutput must beEqualTo(Vector(MessageFactory.appOptionsMessage))
        result must beEqualTo(Quit.asRight)
      }
    }

    "When invalid input is provided" >> {
      "When input is '4' it should be failed and return ParseFailure as Left" >> {
        IdConsole.dummyInput = Vector("4")

        val result: Id[Either[model.AppError, ApplicationOptionCommand]] = idProgram.processSelectApplicationOptions()

        IdConsole.dummyOutput must beEqualTo(Vector(MessageFactory.appOptionsMessage))
        result must beEqualTo(ParseFailure("Cannot parse '4' as ApplicationOptionCommand").asLeft)
      }
    }
  }

  "processSelectSearchObject" >> {
    "When valid input is provided" >> {
      "When input is '1' it should parse as SearchUsers" >> {
        IdConsole.dummyInput = Vector("1")

        val result: Id[Either[model.AppError, SearchObjectCommand]] = idProgram.processSelectSearchObject()

        IdConsole.dummyOutput must beEqualTo(Vector(MessageFactory.searchObjectsOptionMessage))
        result must beEqualTo(SearchUsers.asRight)
      }

      "When input is '2' it should parse as SearchTickets" >> {
        IdConsole.dummyInput = Vector("2")

        val result: Id[Either[model.AppError, SearchObjectCommand]] = idProgram.processSelectSearchObject()

        IdConsole.dummyOutput must beEqualTo(Vector(MessageFactory.searchObjectsOptionMessage))
        result must beEqualTo(SearchTickets.asRight)
      }

      "When input is '3' it should parse as SearchOrganizations" >> {
        IdConsole.dummyInput = Vector("3")

        val result: Id[Either[model.AppError, SearchObjectCommand]] = idProgram.processSelectSearchObject()

        IdConsole.dummyOutput must beEqualTo(Vector(MessageFactory.searchObjectsOptionMessage))
        result must beEqualTo(SearchOrganizations.asRight)
      }

      "When input is 'quit' it should parse as SearchOptionCommand" >> {
        IdConsole.dummyInput = Vector("quit")

        val result: Id[Either[model.AppError, SearchObjectCommand]] = idProgram.processSelectSearchObject()

        IdConsole.dummyOutput must beEqualTo(Vector(MessageFactory.searchObjectsOptionMessage))
        result must beEqualTo(SearchObjectCommand.Quit.asRight)
      }
    }

    "When invalid input is provided" >> {
      "When input is '4' it should be failed and return ParseFailure as Left" >> {
        IdConsole.dummyInput = Vector("4")

        val result: Id[Either[model.AppError, SearchObjectCommand]] = idProgram.processSelectSearchObject()

        IdConsole.dummyOutput must beEqualTo(Vector(MessageFactory.searchObjectsOptionMessage))
        result must beEqualTo(ParseFailure("Cannot parse '4' as SearchObjectCommand").asLeft)
      }
    }
  }
}
