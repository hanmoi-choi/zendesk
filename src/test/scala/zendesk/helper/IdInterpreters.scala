package zendesk.helper

import cats.Id
import cats.syntax.either._
import zendesk.dsl.{Console, Repository, UserInputParser}
import zendesk.model.{AppError, Database, QueryParams, SearchResult}
import zendesk.service.QueryParameterGenerator
import zendesk.service.parser.{Parser, SearchObjectCommand}

import scala.collection.mutable
import scala.collection.mutable.{Queue => MQueue}

object IdInterpreters {

  implicit object IdConsole extends Console[Id] {
    var dummyInput: MQueue[String] = MQueue.empty
    var dummyOutput: mutable.Queue[String] = MQueue.empty

    def resetInputAndOutput(): Unit = {
      dummyInput = MQueue.empty
      dummyOutput = MQueue.empty
    }

    override def out(message: String): Id[Either[AppError, Unit]] = {
      dummyOutput.enqueue(message)
      Right(())
    }

    override def in(): Id[Either[AppError, String]] = {
      dummyInput.dequeue().asRight
    }
  }

  implicit object IdEitherUserInputParser extends UserInputParser[Id] {
    override def parseSearchObject(value: String)(
      implicit P: Parser[SearchObjectCommand]
    ): Id[Either[AppError, SearchObjectCommand]] = {
      P.doParse(value)
    }

    override def parseSearchQuery(searchObjectCommand: SearchObjectCommand, termToSearch: String, searchValue: String)(
      implicit G: QueryParameterGenerator): Id[Either[AppError, QueryParams]] =
      G.generate(searchObjectCommand, termToSearch, searchValue)
  }

  implicit object IORepository extends Repository[Id] {
    import zendesk.dsl.Repository.queryImpl

    override def query(params: QueryParams)(implicit DB: Database): Id[Either[AppError, Vector[SearchResult]]] =
      queryImpl(params)
  }
}
