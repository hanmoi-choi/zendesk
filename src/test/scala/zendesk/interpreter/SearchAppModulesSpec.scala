package zendesk.interpreter

import cats.Id
import cats.syntax.either._
import org.specs2.mutable.Specification
import org.specs2.specification.BeforeEach
import zendesk.helper.IdInterpreters._
import zendesk.helper.TestDataFactory
import zendesk.model
import zendesk.model._
import zendesk.service.QueryParameterGenerator
import zendesk.service.parser.SearchObjectCommand.{SearchOrganizations, SearchTickets, SearchUsers}
import zendesk.service.parser.{SearchObjectCommand, SearchObjectCommandParser}
import zendesk.util.MessageFactory._
import zendesk.util.DataFileReader

import scala.collection.mutable.{Queue => MQueue}

class SearchAppModulesSpec extends Specification with BeforeEach {
  // Examples should be executed sequencially due to dummyInput and dummyOutput state.
  sequential

  private val users = DataFileReader.getDataFromFile[User]("./data/users.json")
  private val orgs = DataFileReader.getDataFromFile[Organization]("./data/organizations.json")
  private val tickets = DataFileReader.getDataFromFile[Ticket]("./data/tickets.json")

  implicit private val searchObjectCommandParser = SearchObjectCommandParser()
  implicit private val queryParameterGenerator = QueryParameterGenerator()
  implicit private val database = Database(userData = users, organizationData = orgs, ticketData = tickets)

  val idProgram = new SearchAppModules[Id]()

  override protected def before: Any = IdConsole.resetInputAndOutput()

  "processSelectSearchObject" >> {
    "When valid input is provided" >> {
      "When input is '1' it should parse as SearchUsers" >> {
        IdConsole.dummyInput = MQueue("1")

        val result: Id[Either[model.AppError, SearchObjectCommand]] = idProgram.processSelectSearchObject()

        IdConsole.dummyOutput must beEqualTo(
          MQueue(searchObjectsOptionMessage, TestDataFactory.listOfSearchableFieldsForUsers))
        result must beEqualTo(SearchUsers.asRight)
      }

      "When input is '2' it should parse as SearchTickets" >> {
        IdConsole.dummyInput = MQueue("2")

        val result: Id[Either[model.AppError, SearchObjectCommand]] = idProgram.processSelectSearchObject()

        IdConsole.dummyOutput must beEqualTo(
          MQueue(searchObjectsOptionMessage, TestDataFactory.listOfSearchableFieldsForTickets))
        result must beEqualTo(SearchTickets.asRight)
      }

      "When input is '3' it should parse as SearchOrganizations" >> {
        IdConsole.dummyInput = MQueue("3")

        val result: Id[Either[model.AppError, SearchObjectCommand]] = idProgram.processSelectSearchObject()

        IdConsole.dummyOutput must beEqualTo(
          MQueue(searchObjectsOptionMessage, TestDataFactory.listOfSearchableFieldsForOrganizations))
        result must beEqualTo(SearchOrganizations.asRight)
      }

      "When input is 'quit' it should parse as SearchOptionCommand" >> {
        IdConsole.dummyInput = MQueue("quit")

        val result: Id[Either[model.AppError, SearchObjectCommand]] = idProgram.processSelectSearchObject()

        IdConsole.dummyOutput must beEqualTo(MQueue(searchObjectsOptionMessage))
        result must beEqualTo(ExitAppByUserRequest.asLeft)
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

//  "searchData" >> {
//    "When valid inputs are entered" >> {
//      "Inputs are for searching Users with term('id') and value('1')" >> {
//        val expectedUser = User(
//          Id(1),
//          Url("http://initech.zendesk.com/api/v2/users/1.json"),
//          ExternalId(UUID.fromString("74341f74-9c79-49d5-9611-87ef9b6eb75f")),
//          Name("Francisca Rasmussen"),
//          Some(Alias("Miss Coffey")),
//          ZenDateTime(DateTime.parse("2016-04-15T05:19:46-10:00")),
//          Active(true),
//          Some(Verified(true)),
//          Shared(false),
//          Some(Locale("en-AU")),
//          Some(Timezone("Sri Lanka")),
//          ZenDateTime(DateTime.parse("2013-08-04T01:03:27-10:00")),
//          Some(Email("coffeyrasmussen@flotonic.com")),
//          Phone("8335-422-718"),
//          Signature("Don't Worry Be Happy!"),
//          Some(OrganizationId(119)),
//          List(Tag("Springville"), Tag("Sutton"), Tag("Hartsville/Hartley"), Tag("Diaperville")),
//          Suspended(true),
//          Admin
//        )
//
//        val queryParams = QueryParams(Searchable.Users, "Id", zendesk.model.value.Id(1L))
//        val result: Id[Either[model.AppError, Vector[Searchable]]] = idProgram.searchData(queryParams)
//
//        result must beEqualTo(Vector(expectedUser).asRight)
//      }
//
//      "Inputs are for searching Tickets with term('Id') and value('436bf9b0-1147-4c0a-8439-6f79833bff5b')" >> {
//        val expectedTicket = Ticket(
//          TicketId(UUID.fromString("436bf9b0-1147-4c0a-8439-6f79833bff5b")),
//          Url("http://initech.zendesk.com/api/v2/tickets/436bf9b0-1147-4c0a-8439-6f79833bff5b.json"),
//          ExternalId(UUID.fromString("9210cdc9-4bee-485f-a078-35396cd74063")),
//          ZenDateTime(DateTime.parse("2016-04-28T11:19:34-10:00")),
//          Some(Incident),
//          Subject("A Catastrophe in Korea (North)"),
//          Some(Description(
//            "Nostrud ad sit velit cupidatat laboris ipsum nisi amet laboris ex exercitation amet et proident. Ipsum fugiat aute dolore tempor nostrud velit ipsum.")),
//          High,
//          Pending,
//          SubmitterId(38),
//          Some(AssigneeId(24)),
//          Some(OrganizationId(116)),
//          List(Tag("Ohio"), Tag("Pennsylvania"), Tag("American Samoa"), Tag("Northern Mariana Islands")),
//          HasIncidents(false),
//          Some(ZenDateTime(DateTime.parse("2016-07-31T02:37:50-10:00"))),
//          Web
//        )
//
//        val queryParams = QueryParams(
//          Searchable.Tickets,
//          "Id",
//          zendesk.model.value.TicketId(UUID.fromString("436bf9b0-1147-4c0a-8439-6f79833bff5b")))
//        val result: Id[Either[model.AppError, Vector[Searchable]]] = idProgram.searchData(queryParams)
//
//        result must beEqualTo(Vector(expectedTicket).asRight)
//      }
//
//      "Inputs are for searching Organizations with term('id') and value('101')" >> {
//        val expectedOrg = Organization(
//          Id(101),
//          Url("http://initech.zendesk.com/api/v2/organizations/101.json"),
//          ExternalId(UUID.fromString("9270ed79-35eb-4a38-a46f-35725197ea8d")),
//          Name("Enthaze"),
//          List(DomainName("kage.com"), DomainName("ecratic.com"), DomainName("endipin.com"), DomainName("zentix.com")),
//          ZenDateTime(DateTime.parse("2016-05-21T11:10:28-10:00")),
//          Details("MegaCorp"),
//          SharedTickets(false),
//          List(Tag("Fulton"), Tag("West"), Tag("Rodriguez"), Tag("Farley"))
//        )
//        val queryParams = QueryParams(Searchable.Organizations, "Id", zendesk.model.value.Id(101L))
//        val result: Id[Either[model.AppError, Vector[Searchable]]] = idProgram.searchData(queryParams)
//
//        result must beEqualTo(Vector(expectedOrg).asRight)
//      }
//    }
//
//    "When invalid inputs are entered" >> {
//      "Inputs are for searching Users with term('id') and value('111111111111111')" >> {
//        val dataNotFound = DataNotfound("Users with Term('Id') and Value('Id(111111111111111)') is not found")
//        val queryParams = QueryParams(Searchable.Users, "Id", zendesk.model.value.Id(111111111111111L))
//        val result: Id[Either[model.AppError, Vector[Searchable]]] = idProgram.searchData(queryParams)
//
//        result must beEqualTo(dataNotFound.asLeft)
//      }
//
//      "Inputs are for searching Tickets with term('submitterId') and value('111111111111111')" >> {
//        val dataNotFound =
//          DataNotfound("Tickets with Term('SubmitterId') and Value('SubmitterId(111111111111111)') is not found")
//        val queryParams =
//          QueryParams(Searchable.Tickets, "SubmitterId", zendesk.model.value.SubmitterId(111111111111111L))
//        val result: Id[Either[model.AppError, Vector[Searchable]]] = idProgram.searchData(queryParams)
//
//        result must beEqualTo(dataNotFound.asLeft)
//      }
//
//      "Inputs are for searching Organizations with term('id') and value('111111111111111')" >> {
//        val dataNotFound = DataNotfound("Organizations with Term('Id') and Value('Id(111111111111111)') is not found")
//        val queryParams = QueryParams(Searchable.Organizations, "Id", zendesk.model.value.Id(111111111111111L))
//        val result: Id[Either[model.AppError, Vector[Searchable]]] = idProgram.searchData(queryParams)
//
//        result must beEqualTo(dataNotFound.asLeft)
//      }
//    }
//  }
}
