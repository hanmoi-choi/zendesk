package zendesk

import cats.effect.{ExitCode, IO, IOApp}
import zendesk.interpreter.IOInterpreter._
import zendesk.interpreter.SearchAppModules
import zendesk.model.{Organization, Ticket, User}
import zendesk.service.QueryParameterGenerator
import zendesk.service.parser.SearchObjectCommandParser
import zendesk.util.{DataFileReader, MessageFactory, SearchDatabase}

import scala.annotation.tailrec

object ZendeskSearchApp extends IOApp {
  private val users = DataFileReader.getDataFromFile[User]("./data/users.json")
  private val orgs = DataFileReader.getDataFromFile[Organization]("./data/organizations.json")
  private val tickets = DataFileReader.getDataFromFile[Ticket]("./data/tickets.json")

  implicit private val searchObjectCommandParser = SearchObjectCommandParser()
  implicit private val queryParameterGenerator = QueryParameterGenerator()
  implicit private val database = SearchDatabase(userData = users, organizationData = orgs, ticketData = tickets)

  override def run(args: List[String]): IO[ExitCode] = {
    val modules = new SearchAppModules[IO]()

    Console.println(MessageFactory.welcomeMessage)
    program(modules)

    IO(ExitCode.Success)
  }

//  @tailrec
  def program(module: SearchAppModules[IO]): Unit = {
    ???
//    val option = module.processSelectApplicationOptions().unsafeRunSync()
//
//    option match {
//      case Right(ApplicationZendesk) => System.out.println("hi")
//      case Right(ViewSearchableFields) =>
//        program(module)
//      case Right(Quit) => System.exit(-1)
//    }
    //    val r: EitherT[IO, model.AppError, ExitCode] = for {
//      _            <- EitherT()
//      searchObject <- EitherT(module.processSelectSearchObject())
//      queryParams  <- EitherT(module.processCreateQueryParams(searchObject))
//      _            <- EitherT(IO { Console.println(queryParams); ().asRight[model.AppError] })
//      searchResult <- EitherT(moduels.searchData(queryParams))
//      _            <- EitherT(IO { Console.println(searchResult); ().asRight[model.AppError] })
//    } yield ExitCode.Success
  }
}
