package zendesk

import cats.data.EitherT
import cats.effect.{ExitCode, IO, IOApp}
import zendesk.interpreter.IOInterpreter._
import zendesk.interpreter.SearchAppModules
import zendesk.model._
import zendesk.service.parser.SearchObjectCommandParser
import zendesk.service.{QueryParameterGenerator, SearchResultFormatter}
import zendesk.util.{DataBaseGenerator, MessageFactory}

object ZendeskSearchApp extends IOApp {
  implicit private val searchObjectCommandParser = SearchObjectCommandParser()
  implicit private val queryParameterGenerator = QueryParameterGenerator()
  implicit private val formatter = SearchResultFormatter()

  override def run(args: List[String]): IO[ExitCode] = {
    printWelcomeMessage()

    DataBaseGenerator().generateDatabaseWithProgramArguments(args) match {
      case Left(error) => Console.println(AppError.asText(error))
      case Right(value) =>
        implicit val database: Database = value
        val modules = new SearchAppModules[IO]()
        impurityProgramExecution(modules)
    }

    IO(ExitCode.Success)
  }

  // TODO: Admittedly this function code is not pure. This will need to be refactored later.
  private def impurityProgramExecution(modules: SearchAppModules[IO]): Unit = {
    while (true) {
      program(modules).unsafeRunSync() match {
        case Left(ExitAppByUserRequest) =>
          System.exit(0)
        case Left(error) =>
          Console.println(AppError.asText(error))
        case _ => ()
      }
    }
  }

  private def printWelcomeMessage(): Unit =
    Console.println(MessageFactory.welcomeMessage)

  private def program(module: SearchAppModules[IO]): IO[Either[AppError, Unit]] = {
    val r: EitherT[IO, model.AppError, Unit] = for {
      searchObject <- EitherT(module.processSelectSearchObject())
      queryParams  <- EitherT(module.processCreateQueryParams(searchObject))
      _            <- EitherT(module.searchData(queryParams))
    } yield ()

    r.value
  }

}
