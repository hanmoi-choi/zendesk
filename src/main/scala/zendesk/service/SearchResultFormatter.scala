package zendesk.service

import io.circe.syntax._
import zendesk.model.SearchResult.{AssigneeId, ForeignKey, OrganizationIdAtTicket, OrganizationIdAtUser, SubmitterId}
import zendesk.model.Searchable.{Tickets, Users}
import zendesk.model.{Organization, QueryParams, SearchResult, Searchable, Ticket, User}

case class SearchResultFormatter() {
  def format(searchResults: Vector[SearchResult]): String = {
    val reportTitle = searchResults.headOption.map { sr: SearchResult =>
      title(sr.queryParams)
    }.toVector

    val reportContentsForPrimaryObject =
      searchResults.map { sr: SearchResult =>
        s"""## Primary Object
           |${sr.primaryObject.asFullDataString}
           |
           |${formatRelations(sr.queryParams, sr.relations)}
           |---------------------------------------------------""".stripMargin
      }

    (reportTitle ++ reportContentsForPrimaryObject).mkString("")
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

  private def formatRelations(queryParams: QueryParams, relations: Map[ForeignKey, Vector[Searchable]]): String = {
    val header = "## Relations"

    val descriptions = relations.toVector.map { relation: (ForeignKey, Vector[Searchable]) =>
      val body = relation._2.map(_.asSimpleDataString).mkString("")

      s"""${subHeader(queryParams.searchKey, relation._1)}
         |$body
         |
         |""".stripMargin
    }.mkString("")

    s"""$header
       |$descriptions""".stripMargin
  }

  private def subHeader(value: (Searchable.Keys, ForeignKey)): String = value match {
    case (Users, SubmitterId) => s"### Tickets as Submitter"
    case (Tickets, SubmitterId) => s"### Users as Submitter"
    case (Users, AssigneeId) => s"### Tickets as Assignee"
    case (Tickets, AssigneeId) => s"### Users as Assignee"
    case (_, OrganizationIdAtTicket) | (_, OrganizationIdAtUser) => "### Organizations"
    case _ => ""
  }
}
