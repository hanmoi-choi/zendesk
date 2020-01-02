package zendesk.service

import io.circe.syntax._
import zendesk.model.{Organization, QueryParams, SearchResult, Searchable, Ticket, User}

case class SearchResultFormatter(searchResults: Vector[SearchResult]) {
  def format(): String = {
    searchResults.flatMap { sr: SearchResult =>
      Vector(
        title(sr.queryParams),
        formatPrimaryObject(sr.queryParams, sr.primaryObject)
      )
    }.mkString("")
  }

  private def formatPrimaryObject(queryParams: QueryParams, primaryObject: Searchable) = {
    val formatted = queryParams.searchKey match {
      case Searchable.Users =>
        primaryObject
          .asInstanceOf[User]
          .asJson
          .spaces2
          .replaceAll("\"", "")
          .replaceAll(",", "")
          .replaceAll("\\}", "")
          .trim
          .replaceAll("\\{", "")

      case Searchable.Tickets =>
        primaryObject
          .asInstanceOf[Ticket]
          .asJson
          .spaces2
          .replaceAll("\"", "")
          .replaceAll(",", "")
          .replaceAll("\\}", "")
          .trim
          .replaceAll("\\{", "")

      case Searchable.Organizations =>
        primaryObject
          .asInstanceOf[Organization]
          .asJson
          .spaces2
          .replaceAll("\"", "")
          .replaceAll(",", "")
          .replaceAll("\\}", "")
          .trim
          .replaceAll("\\{", "")
    }

    s"""$formatted
       |---------------------------------------------------""".stripMargin
  }

  private def title(queryParams: QueryParams): String = {
    s"""
       |---------------------------------------------------
       | Search Results for this parameters
       |   - Object: ${queryParams.searchKey}
       |   - Term: ${queryParams.searchTerm}
       |   - Value: ${queryParams.searchValue.rawValue}
       |---------------------------------------------------
       |""".stripMargin
  }
}
