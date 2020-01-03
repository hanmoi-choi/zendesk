package zendesk

import cats.data.EitherT
import cats.effect.{ExitCode, IO, IOApp}
import zendesk.interpreter.IOInterpreter._
import zendesk.interpreter.SearchAppModules
import zendesk.model._
import zendesk.service.parser.SearchObjectCommandParser
import zendesk.service.{QueryParameterGenerator, SearchResultFormatter}
import zendesk.util.{DataFileReader, MessageFactory}

object ZendeskSearchApp extends IOApp {
  private val defaultUsersData = "./data/users.json"
  private val defaultOrganizationsData = "./data/organizations.json"
  private val defaultTicketsData = "./data/tickets.json"

  private val users = DataFileReader.getDataFromFile[User](defaultUsersData)
  private val organizations = DataFileReader.getDataFromFile[Organization](defaultOrganizationsData)
  private val tickets = DataFileReader.getDataFromFile[Ticket](defaultTicketsData)

  implicit private val searchObjectCommandParser = SearchObjectCommandParser()
  implicit private val queryParameterGenerator = QueryParameterGenerator()
  implicit private val database = Database(userData = users, organizationData = organizations, ticketData = tickets)
  implicit private val formatter = SearchResultFormatter()

  /*
   Admittedly this function code is not pure.
   This will need to be refactored later.
   */
  override def run(args: List[String]): IO[ExitCode] = {
    val modules = new SearchAppModules[IO]()

    Console.println(MessageFactory.welcomeMessage)

    val result = program(modules)

    result.map {
      case Right(_) => ExitCode.Success
      case _ => ExitCode.Error
    }
  }

  def program(module: SearchAppModules[IO]): IO[Either[AppError, Unit]] = {
    val r: EitherT[IO, model.AppError, Unit] = for {
      searchObject <- EitherT(module.processSelectSearchObject())
      queryParams  <- EitherT(module.processCreateQueryParams(searchObject))
      _            <- EitherT(module.searchData(queryParams))
    } yield ()

    r.value
  }
}
