package zendesk

import cats.data.EitherT
import cats.syntax.either._
import cats.effect.{ExitCode, IO, IOApp}
import zendesk.interpreter.IOInterpreter._
import zendesk.interpreter.SearchAppSubModules
import zendesk.service.QueryParameterGenerator
import zendesk.service.parser.{ApplicationOptionCommandParser, SearchObjectCommandParser}
import zendesk.util.MessageFactory

object ZendeskSearchApp extends IOApp {
  implicit private val applicationOptionCommandParser = ApplicationOptionCommandParser()
  implicit private val searchObjectCommandParser = SearchObjectCommandParser()
  implicit private val queryParameterGenerator = QueryParameterGenerator()

  override def run(args: List[String]): IO[ExitCode] = {

    val moduels = new SearchAppSubModules[IO]()

    Console.println(MessageFactory.welcomeMessage)

    val r: EitherT[IO, model.AppError, ExitCode] = for {
      _            <- EitherT(moduels.processSelectApplicationOptions())
      searchObject <- EitherT(moduels.processSelectSearchObject())
      queryParams  <- EitherT(moduels.processCreateQueryParams(searchObject))
      _            <- EitherT(IO { Console.println(queryParams); ().asRight[model.AppError] })
    } yield ExitCode.Success

    val a: IO[ExitCode] = r.value.map {
      case Left(_) => ExitCode.Error
      case Right(_) => ExitCode.Success
    }
    a
  }
}
