package zendesk.dsl

import cats.syntax.either._
import zendesk.model.SearchResult.ForeignKey
import zendesk.model.value.{AssigneeId, Id, OrganizationId, SubmitterId}
import zendesk.model.{Database, _}

trait Repository[F[_]] {
  def query(params: QueryParams)(implicit DB: Database): F[Either[AppError, Vector[SearchResult]]]
}

object Repository {
  def apply[F[_]](implicit R: Repository[F]): Repository[F] = R

  def query[F[_]: Repository](params: QueryParams)(implicit DB: Database): F[Either[AppError, Vector[SearchResult]]] =
    Repository[F].query(params)

  def queryImpl(params: QueryParams)(implicit DB: Database): Either[DataNotfound, Vector[SearchResult]] = {
    DB.query(params) match {
      case Vector() =>
        val errorMessage =
          s"${params.searchKey} with Term('${params.searchTerm}') and Value('${params.searchValue.rawValue}') is not found"
        DataNotfound(errorMessage).asLeft

      case nonEmptyResult =>
        retrieveRelations(params, nonEmptyResult)
    }
  }

  private def retrieveRelations(params: QueryParams, nonEmptyResult: Vector[Searchable])(
    implicit DB: Database): Either[DataNotfound, Vector[SearchResult]] = {
    params.searchKey match {
      case Searchable.Users =>
        nonEmptyResult.asInstanceOf[Vector[User]].map(v => retrieveRelationsForUser(params, v)).asRight

      case Searchable.Tickets =>
        nonEmptyResult.asInstanceOf[Vector[Ticket]].map(v => retrieveRelationsForTicket(params, v)).asRight

      case Searchable.Organizations =>
        nonEmptyResult.asInstanceOf[Vector[Organization]].map(v => retrieveRelationsForOrganization(params, v)).asRight
    }
  }

  private def retrieveRelationsForUser(params: QueryParams, user: User)(implicit DB: Database): SearchResult = {
    val submitterId = SubmitterId(user.id.value)
    val assigneeId = AssigneeId(user.id.value)

    val relationsForTickets = Map[ForeignKey, Vector[Searchable]](
      SearchResult.SubmitterId -> DB.query[Ticket](
        QueryParams(Searchable.Tickets, submitterId.getClass.getSimpleName, submitterId)),
      SearchResult.AssigneeId ->
        DB.query[Ticket](QueryParams(Searchable.Tickets, assigneeId.getClass.getSimpleName, assigneeId))
    )

    val maybeRelations = user.organizationId.map { orgId =>
      val id = Id(orgId.value)
      Map(
        SearchResult.OrganizationIdAtUser ->
          DB.query[Organization](QueryParams(Searchable.Organizations, id.getClass.getSimpleName, id))
      )
    }.getOrElse(Map.empty)

    SearchResult(params, user: Searchable, relationsForTickets ++ maybeRelations)
  }

  private def retrieveRelationsForOrganization(params: QueryParams, org: Organization)(
    implicit DB: Database): SearchResult = {
    val orgId = OrganizationId(org.id.value)
    val relations = Map[ForeignKey, Vector[Searchable]](
      SearchResult.OrganizationIdAtUser -> DB.query[User](
        QueryParams(Searchable.Users, orgId.getClass.getSimpleName, orgId)),
      SearchResult.OrganizationIdAtTicket ->
        DB.query[Ticket](QueryParams(Searchable.Tickets, orgId.getClass.getSimpleName, orgId))
    )

    SearchResult(params, org: Searchable, relations)
  }

  private def retrieveRelationsForTicket(params: QueryParams, ticket: Ticket)(implicit DB: Database): SearchResult = {
    val submitterId = Id(ticket.submitterId.value)
    val relationsForUsers = Map[ForeignKey, Vector[Searchable]](
      (
        SearchResult.SubmitterId ->
          DB.query[User](QueryParams(Searchable.Users, submitterId.getClass.getSimpleName, submitterId))
      )
    )

    val maybeRelationsForUser = ticket.assigneeId.map { assigneeId =>
      val id = Id(assigneeId.value)
      Map(
        SearchResult.AssigneeId ->
          DB.query[User](QueryParams(Searchable.Users, id.getClass.getSimpleName, id))
      )
    }.getOrElse(Map.empty)

    val maybeRelationsForOrg = ticket.organizationId.map { orgId =>
      val id = Id(orgId.value)
      Map(
        SearchResult.OrganizationIdAtTicket ->
          DB.query[Organization](QueryParams(Searchable.Organizations, id.getClass.getSimpleName, id))
      )
    }.getOrElse(Map.empty)

    SearchResult(params, ticket: Searchable, relationsForUsers ++ maybeRelationsForUser ++ maybeRelationsForOrg)
  }
}
