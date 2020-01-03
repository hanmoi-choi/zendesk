package zendesk.util

import cats.syntax.either._
import io.circe.Decoder
import io.circe.parser.decode
import zendesk.model.FileNotExistError

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

  def getDataFromFile[T](path: String)(implicit decoder: Decoder[T]): Vector[T] = {
    readFileAsString(path).flatMap { rawJson =>
      decode[List[T]](rawJson)
    }.getOrElse(List.empty).toVector
  }
}
