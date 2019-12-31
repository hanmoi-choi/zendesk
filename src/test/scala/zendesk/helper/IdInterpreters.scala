package zendesk.helper

import cats.Id
import cats.syntax.either._
import zendesk.dsl.{Console, UserInputParser}
import zendesk.model.AppError
import zendesk.util.parser.{Parser, SearchOptionCommand}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object IdInterpreters {

  implicit object IdConsole extends Console[Id] {
    var dummyInput: Vector[String] = Vector.empty
    var dummyOutput: mutable.ListBuffer[String] = ListBuffer.empty

    def resetInputAndOutput(): Unit = {
      dummyInput = Vector.empty
      dummyOutput = ListBuffer.empty
    }

    override def out(string: String): Id[Either[AppError, Unit]] = {
      dummyOutput.addOne(string)
      Right(())
    }

    override def in(): Id[Either[AppError, String]] = {
      dummyInput.head.asRight[AppError]
    }
  }

  implicit object IdEitherUserInputParser extends UserInputParser[Id] {
    override def parseSearchOption(value: String)
                                  (implicit P: Parser[SearchOptionCommand]
                                  ): Id[Either[AppError, SearchOptionCommand]] = {
      P.doParse(value)
    }
  }
}
