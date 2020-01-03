package zendesk.interpreter

import java.util.UUID

import cats.Id
import cats.syntax.either._
import org.specs2.mutable.Specification
import org.specs2.specification.BeforeEach
import zendesk.helper.IdInterpreters._
import zendesk.helper.TestDataFactory
import zendesk.model._
import zendesk.service.parser.SearchObjectCommand.{SearchOrganizations, SearchTickets, SearchUsers}
import zendesk.service.parser.SearchObjectCommandParser
import zendesk.service.{QueryParameterGenerator, SearchResultFormatter}
import zendesk.util.DataBaseGenerator
import zendesk.util.MessageFactory._

import scala.collection.mutable.{Queue => MQueue}

class SearchAppModulesSpec extends Specification with BeforeEach {
  // Examples should be executed sequencially due to dummyInput and dummyOutput state.
  sequential

  implicit private val searchObjectCommandParser = SearchObjectCommandParser()
  implicit private val queryParameterGenerator = QueryParameterGenerator()
  implicit private val database =
    DataBaseGenerator().generateDatabaseWithProgramArguments(List.empty).getOrElse(Database())
  implicit private val formatter = SearchResultFormatter()

  val idProgram = new SearchAppModules[Id]()

  override protected def before: Any = IdConsole.resetInputAndOutput()

  "processSelectSearchObject" >> {
    "When valid input is provided" >> {
      "When input is '1' it should parse as SearchUsers" >> {
        IdConsole.dummyInput = MQueue("1")

        val result = idProgram.processSelectSearchObject()

        IdConsole.dummyOutput must beEqualTo(
          MQueue(searchObjectsOptionMessage, TestDataFactory.listOfSearchableFieldsForUsers))
        result must beEqualTo(SearchUsers.asRight)
      }

      "When input is '2' it should parse as SearchTickets" >> {
        IdConsole.dummyInput = MQueue("2")

        val result = idProgram.processSelectSearchObject()

        IdConsole.dummyOutput must beEqualTo(
          MQueue(searchObjectsOptionMessage, TestDataFactory.listOfSearchableFieldsForTickets))
        result must beEqualTo(SearchTickets.asRight)
      }

      "When input is '3' it should parse as SearchOrganizations" >> {
        IdConsole.dummyInput = MQueue("3")

        val result = idProgram.processSelectSearchObject()

        IdConsole.dummyOutput must beEqualTo(
          MQueue(searchObjectsOptionMessage, TestDataFactory.listOfSearchableFieldsForOrganizations))
        result must beEqualTo(SearchOrganizations.asRight)
      }

      "When input is 'quit' it should parse as SearchOptionCommand" >> {
        IdConsole.dummyInput = MQueue("quit")

        val result = idProgram.processSelectSearchObject()

        IdConsole.dummyOutput must beEqualTo(MQueue(searchObjectsOptionMessage))
        result must beEqualTo(ExitAppByUserRequest.asLeft)
      }
    }

    "When invalid input is provided" >> {
      "When input is '4' it should be failed and return ParseFailure as Left" >> {
        IdConsole.dummyInput = MQueue("4")

        val result = idProgram.processSelectSearchObject()

        IdConsole.dummyOutput must beEqualTo(MQueue(searchObjectsOptionMessage))
        result must beEqualTo(CommandParseFailure("Cannot parse '4' as SearchObjectCommand").asLeft)
      }
    }
  }

  "processCreateQueryParams" >> {
    "When valid inputs are entered" >> {
      "User enters 'quit'" >> {
        "Inputs are for searching Users with term('quit')" >> {
          IdConsole.dummyInput = MQueue("quit")

          val result = idProgram.processCreateQueryParams(SearchUsers)

          IdConsole.dummyOutput must beEqualTo(MQueue(enterSearchTerm))

          result must beEqualTo(ExitAppByUserRequest.asLeft)
        }

        "Inputs are for searching Users with term('id') and value('quit')" >> {
          IdConsole.dummyInput = MQueue("id", "quit")

          val result = idProgram.processCreateQueryParams(SearchUsers)

          IdConsole.dummyOutput must beEqualTo(MQueue(enterSearchTerm, enterSearchValue))

          result must beEqualTo(ExitAppByUserRequest.asLeft)
        }
      }

      "Inputs are for searching Users with term('id') and value('1')" >> {
        IdConsole.dummyInput = MQueue("id", "1")

        val result = idProgram.processCreateQueryParams(SearchUsers)

        IdConsole.dummyOutput must beEqualTo(MQueue(enterSearchTerm, enterSearchValue))

        result must beEqualTo(QueryParams(Searchable.Users, "Id", zendesk.model.value.Id(1)).asRight)
      }

      "Inputs are for searching Organizations with term('submitterId') and value('1')" >> {
        IdConsole.dummyInput = MQueue("submitterId", "1")

        val result = idProgram.processCreateQueryParams(SearchTickets)

        IdConsole.dummyOutput must beEqualTo(MQueue(enterSearchTerm, enterSearchValue))

        result must beEqualTo(
          QueryParams(Searchable.Tickets, "SubmitterId", zendesk.model.value.SubmitterId(1)).asRight)
      }

      "Inputs are for searching Organizations with term('id') and value('1')" >> {
        IdConsole.dummyInput = MQueue("id", "1")

        val result = idProgram.processCreateQueryParams(SearchOrganizations)

        IdConsole.dummyOutput must beEqualTo(MQueue(enterSearchTerm, enterSearchValue))

        result must beEqualTo(QueryParams(Searchable.Organizations, "Id", zendesk.model.value.Id(1)).asRight)
      }
    }

    "When invalid inputs are entered" >> {
      val invalidArgumentError = InvalidArgumentError("'a' is not Integer value")

      "Inputs are for searching Users with term('id') and value('a')" >> {
        IdConsole.dummyInput = MQueue("id", "a")

        val result = idProgram.processCreateQueryParams(SearchUsers)

        IdConsole.dummyOutput must beEqualTo(MQueue(enterSearchTerm, enterSearchValue))

        result must beEqualTo(invalidArgumentError.asLeft)
      }

      "Inputs are for searching Organizations with term('submitterId') and value('a')" >> {
        IdConsole.dummyInput = MQueue("submitterId", "a")

        val result = idProgram.processCreateQueryParams(SearchTickets)

        IdConsole.dummyOutput must beEqualTo(MQueue(enterSearchTerm, enterSearchValue))

        result must beEqualTo(invalidArgumentError.asLeft)
      }

      "Inputs are for searching Organizations with term('id') and value('a')" >> {
        IdConsole.dummyInput = MQueue("id", "a")

        val result = idProgram.processCreateQueryParams(SearchOrganizations)

        IdConsole.dummyOutput must beEqualTo(MQueue(enterSearchTerm, enterSearchValue))

        result must beEqualTo(invalidArgumentError.asLeft)
      }
    }
  }

  "searchData" >> {
    "When valid inputs are entered" >> {
      "Inputs are for searching Users with term('id') and value('1')" >> {
        val queryParams = QueryParams(Searchable.Users, "Id", zendesk.model.value.Id(1L))

        idProgram.searchData(queryParams)
        IdConsole.dummyOutput must beEqualTo(MQueue(TestDataFactory.expectedUserSearchResult))
      }

      "Inputs are for searching Tickets with term('Id') and value('436bf9b0-1147-4c0a-8439-6f79833bff5b')" >> {
        val queryParams = QueryParams(
          Searchable.Tickets,
          "Id",
          zendesk.model.value.TicketId(UUID.fromString("436bf9b0-1147-4c0a-8439-6f79833bff5b")))

        idProgram.searchData(queryParams)
        IdConsole.dummyOutput must beEqualTo(MQueue(TestDataFactory.expectedTicketSearchResult))
      }

      "Inputs are for searching Organizations with term('id') and value('101')" >> {
        val queryParams = QueryParams(Searchable.Organizations, "Id", zendesk.model.value.Id(101L))

        idProgram.searchData(queryParams)
        IdConsole.dummyOutput must beEqualTo(MQueue(TestDataFactory.expectedOrgSearchResult))
      }
    }

    "When invalid inputs are entered" >> {
      "Inputs are for searching Users with term('id') and value('111111111111111')" >> {
        val dataNotFound = DataNotfound("Users with Term('Id') and Value('111111111111111') is not found")
        val queryParams = QueryParams(Searchable.Users, "Id", zendesk.model.value.Id(111111111111111L))

        val result = idProgram.searchData(queryParams)
        result must beEqualTo(dataNotFound.asLeft)
      }

      "Inputs are for searching Tickets with term('submitterId') and value('111111111111111')" >> {
        val dataNotFound =
          DataNotfound("Tickets with Term('SubmitterId') and Value('111111111111111') is not found")
        val queryParams =
          QueryParams(Searchable.Tickets, "SubmitterId", zendesk.model.value.SubmitterId(111111111111111L))

        val result = idProgram.searchData(queryParams)
        result must beEqualTo(dataNotFound.asLeft)
      }

      "Inputs are for searching Organizations with term('id') and value('111111111111111')" >> {
        val dataNotFound = DataNotfound("Organizations with Term('Id') and Value('111111111111111') is not found")
        val queryParams = QueryParams(Searchable.Organizations, "Id", zendesk.model.value.Id(111111111111111L))

        val result = idProgram.searchData(queryParams)
        result must beEqualTo(dataNotFound.asLeft)
      }
    }
  }
}
