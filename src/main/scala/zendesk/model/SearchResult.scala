package zendesk.model

import zendesk.model.SearchResult.ForeignKey

case class SearchResult(
  queryParams: QueryParams,
  primaryObject: Searchable,
  relations: Map[ForeignKey, Vector[Searchable]]
) {}

object SearchResult {
  sealed trait ForeignKey

  case object OrganizationIdAtUser extends ForeignKey
  case object OrganizationIdAtTicket extends ForeignKey
  case object SubmitterId extends ForeignKey
  case object AssigneeId extends ForeignKey
}
