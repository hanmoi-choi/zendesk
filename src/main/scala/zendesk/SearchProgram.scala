package zendesk

import cats.Monad
import cats.data.EitherT
import zendesk.dsl.{Console, UserInputParser}
import zendesk.dsl.Console._
import zendesk.util.parser.ParserImplements.SearchOptionCommandParser
import zendesk.dsl.UserInputParser._
import zendesk.util.MessageFactory
import zendesk.util.parser.SearchOptionCommand

class SearchProgram [F[_]: Monad: Console: UserInputParser] {

  def processSelectSearchOptions(): F[Either[model.AppError, SearchOptionCommand]] ={
    val parserResult: EitherT[F, model.AppError, SearchOptionCommand] = for {
      _ <- EitherT(out(MessageFactory.searchOptions))
      userInput <- EitherT(in())
      result <- EitherT(parseSearchOption(userInput))
    } yield result

    parserResult.value
  }
}
