package zendesk.service

import io.circe.syntax._
import zendesk.model.{Organization, QueryParams, SearchResult, Searchable, Ticket, User}

case class SearchResultFormatter() {
  def format(searchResults: Vector[SearchResult]): String = {
    val reportTitle = searchResults.headOption.map { sr: SearchResult =>
      title(sr.queryParams)
    }.toVector

    val reportContents = searchResults.map { sr: SearchResult =>
      s"""${sr.primaryObject.asFullDataString}
         |---------------------------------------------------""".stripMargin
    }

    (reportTitle ++ reportContents).mkString("")
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
