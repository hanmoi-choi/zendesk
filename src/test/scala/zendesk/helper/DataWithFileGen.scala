package zendesk.helper

import io.circe
import io.circe.Decoder
import io.circe.parser.decode

import scala.io.Source

object DataWithFileGen {
  private def readFileAsString(path: String): String = {
    val fileContents = Source.fromFile(path)
    val rawJson = fileContents.mkString
    fileContents.close()

    rawJson
  }

  def getDataFromFile[T](path: String)(implicit decoder: Decoder[T]): Vector[T] = {
    val rawJson = readFileAsString(path)
    val users: Either[circe.Error, List[T]] = decode[List[T]](rawJson)

    users.getOrElse(List.empty).toVector
  }
}
