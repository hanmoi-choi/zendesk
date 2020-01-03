package zendesk.util

import cats.syntax.either._
import io.circe.Decoder
import io.circe.parser.decode
import zendesk.model.{AppError, FileNotExistError, JsonParseFailure}

import scala.io.Source
import scala.util.{Failure, Success, Try}

object DataFileReader {
  private def readFileAsString(path: String): Either[FileNotExistError, String] = {
    Try(Source.fromFile(path)) match {
      case Success(bs) => bs.mkString.asRight
      case Failure(_) =>
        FileNotExistError(s"File('$path') does not exist").asLeft
    }
  }

  def getDataFromFile[T](path: String)(implicit decoder: Decoder[T]): Either[AppError, Vector[T]] = {
    readFileAsString(path).flatMap { rawJson =>
      decode[Vector[T]](rawJson)
    }.leftMap[AppError] {
      case e @ FileNotExistError(_) => e
      case e => JsonParseFailure(e.toString)
    }
  }
}
