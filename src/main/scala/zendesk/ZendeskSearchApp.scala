package zendesk

import cats.data.EitherT
import cats.syntax.either._
import cats.effect.{ExitCode, IO, IOApp}
import io.circe.syntax._
import zendesk.interpreter.IOInterpreter._
import zendesk.interpreter.SearchAppModules
import zendesk.model.{Organization, Ticket, User}
import zendesk.service.QueryParameterGenerator
import zendesk.service.parser.{ApplicationOptionCommandParser, SearchObjectCommandParser}
import zendesk.util.{DataFileReader, MessageFactory, SearchDatabase}

object ZendeskSearchApp extends IOApp {
  private val users = DataFileReader.getDataFromFile[User]("./data/users.json")
  private val orgs = DataFileReader.getDataFromFile[Organization]("./data/organizations.json")
  private val tickets = DataFileReader.getDataFromFile[Ticket]("./data/tickets.json")

  implicit private val applicationOptionCommandParser = ApplicationOptionCommandParser()
  implicit private val searchObjectCommandParser = SearchObjectCommandParser()
  implicit private val queryParameterGenerator = QueryParameterGenerator()
  implicit private val database = SearchDatabase(userData = users, organizationData = orgs, ticketData = tickets)

  override def run(args: List[String]): IO[ExitCode] = {

    val moduels = new SearchAppModules[IO]()

    Console.println(MessageFactory.welcomeMessage)

    val r: EitherT[IO, model.AppError, ExitCode] = for {
      _            <- EitherT(moduels.processSelectApplicationOptions())
      searchObject <- EitherT(moduels.processSelectSearchObject())
      queryParams  <- EitherT(moduels.processCreateQueryParams(searchObject))
      _            <- EitherT(IO { Console.println(queryParams); ().asRight[model.AppError] })
      searchResult <- EitherT(moduels.searchData(queryParams))
      _            <- EitherT(IO { Console.println(searchResult); ().asRight[model.AppError] })
    } yield ExitCode.Success

    val a: IO[ExitCode] = r.value.map {
      case Left(_) => ExitCode.Error
      case Right(_) => ExitCode.Success
    }
    a
  }
}
