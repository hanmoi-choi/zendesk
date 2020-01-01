package zendesk.interpreter

import cats.Id
import cats.syntax.either._
import org.specs2.mutable.Specification
import org.specs2.specification.BeforeEach
import zendesk.model
import zendesk.model.{InvalidArgumentError, ParseFailure, QueryParams, Searchable}
import zendesk.service.QueryParameterGenerator
import zendesk.service.parser.ApplicationOptionCommand.{ApplicationZendesk, Quit, ViewSearchableFields}
import zendesk.service.parser.SearchObjectCommand.{SearchOrganizations, SearchTickets, SearchUsers}
import zendesk.service.parser.{
  ApplicationOptionCommand,
  ApplicationOptionCommandParser,
  SearchObjectCommand,
  SearchObjectCommandParser
}
import zendesk.util.MessageFactory._
import zendesk.helper.IdInterpreters._

import scala.collection.mutable.{Queue => MQueue}

class SearchAppSubModulesSpec extends Specification with BeforeEach {
  // Examples should be executed sequencially due to dummyInput and dummyOutput state.
  sequential

  implicit private val applicationOptionCommandParser = ApplicationOptionCommandParser()
  implicit private val searchObjectCommandParser = SearchObjectCommandParser()
  implicit private val queryParameterGenerator = QueryParameterGenerator()

  val idProgram = new SearchAppSubModules[Id]()

  override protected def before: Any = IdConsole.resetInputAndOutput()

  "processSelectApplicationOptions" >> {
    "When valid input is provided" >> {
      "When input is '1' it should parse as ApplicationZendesk" >> {
        IdConsole.dummyInput = MQueue("1")

        val result: Id[Either[model.AppError, ApplicationOptionCommand]] = idProgram.processSelectApplicationOptions()

        IdConsole.dummyOutput must beEqualTo(MQueue(appOptionsMessage))
        result must beEqualTo(ApplicationZendesk.asRight)
      }

      "When input is '2' it should parse as ViewSearchableFields" >> {
        IdConsole.dummyInput = MQueue("2")

        val result: Id[Either[model.AppError, ApplicationOptionCommand]] = idProgram.processSelectApplicationOptions()

        IdConsole.dummyOutput must beEqualTo(MQueue(appOptionsMessage))
        result must beEqualTo(ViewSearchableFields.asRight)
      }

      "When input is 'quit' it should parse as Quit" >> {
        IdConsole.dummyInput = MQueue("quit")

        val result: Id[Either[model.AppError, ApplicationOptionCommand]] = idProgram.processSelectApplicationOptions()

        IdConsole.dummyOutput must beEqualTo(MQueue(appOptionsMessage))
        result must beEqualTo(Quit.asRight)
      }
    }

    "When invalid input is provided" >> {
      "When input is '4' it should be failed and return ParseFailure as Left" >> {
        IdConsole.dummyInput = MQueue("4")

        val result: Id[Either[model.AppError, ApplicationOptionCommand]] = idProgram.processSelectApplicationOptions()

        IdConsole.dummyOutput must beEqualTo(MQueue(appOptionsMessage))
        result must beEqualTo(ParseFailure("Cannot parse '4' as ApplicationOptionCommand").asLeft)
      }
    }
  }

  "processSelectSearchObject" >> {
    "When valid input is provided" >> {
      "When input is '1' it should parse as SearchUsers" >> {
        IdConsole.dummyInput = MQueue("1")

        val result: Id[Either[model.AppError, SearchObjectCommand]] = idProgram.processSelectSearchObject()

        IdConsole.dummyOutput must beEqualTo(MQueue(searchObjectsOptionMessage))
        result must beEqualTo(SearchUsers.asRight)
      }

      "When input is '2' it should parse as SearchTickets" >> {
        IdConsole.dummyInput = MQueue("2")

        val result: Id[Either[model.AppError, SearchObjectCommand]] = idProgram.processSelectSearchObject()

        IdConsole.dummyOutput must beEqualTo(MQueue(searchObjectsOptionMessage))
        result must beEqualTo(SearchTickets.asRight)
      }

      "When input is '3' it should parse as SearchOrganizations" >> {
        IdConsole.dummyInput = MQueue("3")

        val result: Id[Either[model.AppError, SearchObjectCommand]] = idProgram.processSelectSearchObject()

        IdConsole.dummyOutput must beEqualTo(MQueue(searchObjectsOptionMessage))
        result must beEqualTo(SearchOrganizations.asRight)
      }

      "When input is 'quit' it should parse as SearchOptionCommand" >> {
        IdConsole.dummyInput = MQueue("quit")

        val result: Id[Either[model.AppError, SearchObjectCommand]] = idProgram.processSelectSearchObject()

        IdConsole.dummyOutput must beEqualTo(MQueue(searchObjectsOptionMessage))
        result must beEqualTo(SearchObjectCommand.Quit.asRight)
      }
    }

    "When invalid input is provided" >> {
      "When input is '4' it should be failed and return ParseFailure as Left" >> {
        IdConsole.dummyInput = MQueue("4")

        val result: Id[Either[model.AppError, SearchObjectCommand]] = idProgram.processSelectSearchObject()

        IdConsole.dummyOutput must beEqualTo(MQueue(searchObjectsOptionMessage))
        result must beEqualTo(ParseFailure("Cannot parse '4' as SearchObjectCommand").asLeft)
      }
    }
  }

  "processCreateQueryParams" >> {
    "When valid inputs are entered" >> {
      "Inputs are for searching Users with term('id') and value('1')" >> {
        IdConsole.dummyInput = MQueue("id", "1")

        val result: Id[Either[model.AppError, QueryParams]] = idProgram.processCreateQueryParams(SearchUsers)

        IdConsole.dummyOutput must beEqualTo(MQueue(enterSearchTerm, enterSearchValue))

        result must beEqualTo(QueryParams(Searchable.Users, "Id", zendesk.model.value.Id(1)).asRight)
      }

      "Inputs are for searching Organizations with term('submitterId') and value('1')" >> {
        IdConsole.dummyInput = MQueue("submitterId", "1")

        val result: Id[Either[model.AppError, QueryParams]] = idProgram.processCreateQueryParams(SearchTickets)

        IdConsole.dummyOutput must beEqualTo(MQueue(enterSearchTerm, enterSearchValue))

        result must beEqualTo(
          QueryParams(Searchable.Tickets, "SubmitterId", zendesk.model.value.SubmitterId(1)).asRight)
      }

      "Inputs are for searching Organizations with term('id') and value('1')" >> {
        IdConsole.dummyInput = MQueue("id", "1")

        val result: Id[Either[model.AppError, QueryParams]] = idProgram.processCreateQueryParams(SearchOrganizations)

        IdConsole.dummyOutput must beEqualTo(MQueue(enterSearchTerm, enterSearchValue))

        result must beEqualTo(QueryParams(Searchable.Organizations, "Id", zendesk.model.value.Id(1)).asRight)
      }
    }

    "When invalid inputs are entered" >> {
      val invalidArgumentError = InvalidArgumentError("'a' is not Integer value")

      "Inputs are for searching Users with term('id') and value('a')" >> {
        IdConsole.dummyInput = MQueue("id", "a")

        val result: Id[Either[model.AppError, QueryParams]] = idProgram.processCreateQueryParams(SearchUsers)

        IdConsole.dummyOutput must beEqualTo(MQueue(enterSearchTerm, enterSearchValue))

        result must beEqualTo(invalidArgumentError.asLeft)
      }

      "Inputs are for searching Organizations with term('submitterId') and value('a')" >> {
        IdConsole.dummyInput = MQueue("submitterId", "a")

        val result: Id[Either[model.AppError, QueryParams]] = idProgram.processCreateQueryParams(SearchTickets)

        IdConsole.dummyOutput must beEqualTo(MQueue(enterSearchTerm, enterSearchValue))

        result must beEqualTo(invalidArgumentError.asLeft)
      }

      "Inputs are for searching Organizations with term('id') and value('a')" >> {
        IdConsole.dummyInput = MQueue("id", "a")

        val result: Id[Either[model.AppError, QueryParams]] = idProgram.processCreateQueryParams(SearchOrganizations)

        IdConsole.dummyOutput must beEqualTo(MQueue(enterSearchTerm, enterSearchValue))

        result must beEqualTo(invalidArgumentError.asLeft)
      }
    }
  }
}
