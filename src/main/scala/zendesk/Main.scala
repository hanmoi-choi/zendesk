package zendesk

import cats.Id
import cats.data.EitherT
import cats.effect.{ExitCode, IO, IOApp}
import cats.instances.string._
import cats.syntax.semigroup._
import zendesk.service.parser.ParserImplementation._
import dsl.Interpreter._

object Main extends IOApp {


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
