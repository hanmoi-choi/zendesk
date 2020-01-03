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

  /*
   Admittedly this function code is not pure.
   This will need to be refactored later.
   */
  override def run(args: List[String]): IO[ExitCode] = {
    printWelcomeMessage()

    implicit val database: Database =
      DataBaseGenerator().generateDatabaseWithProgramArguments(args).getOrElse(Database())

    val modules = new SearchAppModules[IO]()

    val result = program(modules)

    result.map {
      case Right(_) => ExitCode.Success
      case Left(appError) =>
        Console.println(AppError.asText(appError))
        ExitCode.Error
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
