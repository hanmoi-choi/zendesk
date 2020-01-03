package zendesk

import cats.data.EitherT
import cats.effect.{ExitCode, IO, IOApp}
import cats.syntax.either._
import zendesk.interpreter.IOInterpreter._
import zendesk.interpreter.SearchAppModules
import zendesk.model.{AppError, Database, Organization, Ticket, User}
import zendesk.service.{QueryParameterGenerator, SearchResultFormatter}
import zendesk.service.parser.SearchObjectCommandParser
import zendesk.util.{DataFileReader, MessageFactory}

object ZendeskSearchApp extends IOApp {
  private val users = DataFileReader.getDataFromFile[User]("./data/users.json")
  private val orgs = DataFileReader.getDataFromFile[Organization]("./data/organizations.json")
  private val tickets = DataFileReader.getDataFromFile[Ticket]("./data/tickets.json")

  implicit private val searchObjectCommandParser = SearchObjectCommandParser()
  implicit private val queryParameterGenerator = QueryParameterGenerator()
  implicit private val database = Database(userData = users, organizationData = orgs, ticketData = tickets)
  implicit private val formatter = SearchResultFormatter()

  override def run(args: List[String]): IO[ExitCode] = {
    val modules = new SearchAppModules[IO]()

    Console.println(MessageFactory.welcomeMessage)
    val result: IO[Either[AppError, ExitCode]] = program(modules)

    result.map {
      case Right(_) => ExitCode.Success
      case _ => ExitCode.Error
    }
  }

  def program(module: SearchAppModules[IO]): IO[Either[AppError, ExitCode]] = {
    val r: EitherT[IO, model.AppError, ExitCode] = for {
      searchObject <- EitherT(module.processSelectSearchObject())
      queryParams  <- EitherT(module.processCreateQueryParams(searchObject))
      _            <- EitherT(module.searchData(queryParams))
    } yield ExitCode.Success

    r.value
  }
}
