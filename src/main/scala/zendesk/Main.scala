package zendesk

import cats.data.EitherT
import cats.effect.{ExitCode, IO, IOApp}
import zendesk.dsl.Interpreter._
import zendesk.service.parser.{ApplicationOptionCommandParser, SearchObjectCommandParser}

object Main extends IOApp {
  implicit private val applicationOptionCommandParser = ApplicationOptionCommandParser()
  implicit private val searchObjectCommandParser = SearchObjectCommandParser()

  override def run(args: List[String]): IO[ExitCode] = {

    val ioProgram = new SearchProgram[IO]()

    val r: EitherT[IO, model.AppError, ExitCode] = for {
      _ <- EitherT(ioProgram.processSelectApplicationOptions())
      _ <- EitherT(ioProgram.processSelectSearchObject())
    } yield ExitCode.Success

    val a: IO[ExitCode] = r.value.map {
      case Left(_) => ExitCode.Error
      case Right(_) => ExitCode.Success
    }
    a
  }
}
