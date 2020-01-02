package zendesk.dsl

import cats.Monad
import cats.instances.option._
import cats.syntax.either._
import zendesk.model.SearchResult.ForeignKey
import zendesk.model.value.{AssigneeId, Id, OrganizationId, SubmitterId}
import zendesk.model._
import zendesk.util.Database

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
          s"${params.searchKey} with Term('${params.searchTerm}') and Value('${params.searchValue}') is not found"
        DataNotfound(errorMessage).asLeft

      case nonEmptyResult =>
        params.searchKey match {
          case Searchable.Users =>
            nonEmptyResult.map((v: User) => retrieveRelationsForUser(params, v)).asRight

          case Searchable.Tickets =>
            nonEmptyResult.map((v: Ticket) => retrieveRelationsForTicket(params, v)).asRight

          case Searchable.Organizations =>
            nonEmptyResult.map((v: Organization) => retrieveRelationsForOrganization(params, v)).asRight
        }
    }
  }

  private def retrieveRelationsForUser(params: QueryParams, user: User)(implicit DB: Database): SearchResult = {
    val submitterId = SubmitterId(user.id.value)
    val assigneeId = AssigneeId(user.id.value)
    val relations = Map[ForeignKey, Vector[Searchable]](
      (
        SearchResult.SubmitterId,
        DB.query[Ticket](QueryParams(Searchable.Tickets, submitterId.getClass.getSimpleName, submitterId))
      ),
      (
        SearchResult.AssigneeId,
        DB.query[Ticket](QueryParams(Searchable.Tickets, assigneeId.getClass.getSimpleName, assigneeId))
      )
    )
    val maybeRelations = Monad[Option]
      .map(user.organizationId) { orgId =>
        val id = Id(orgId.value)
        Map(
          SearchResult.OrganizationIdAtUser ->
            DB.query[Organization](QueryParams(Searchable.Organizations, id.getClass.getSimpleName, id))
        )
      }
      .getOrElse(Map.empty)

    SearchResult(params, user: Searchable, relations ++ maybeRelations)
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
    val relations = Map[ForeignKey, Vector[Searchable]](
      (
        SearchResult.SubmitterId ->
          DB.query[User](QueryParams(Searchable.Users, submitterId.getClass.getSimpleName, submitterId))
      )
    )

    val maybeRelationsForUser = Monad[Option]
      .map(ticket.assigneeId) { assigneeId =>
        val id = Id(assigneeId.value)
        Map(
          SearchResult.AssigneeId ->
            DB.query[User](QueryParams(Searchable.Users, id.getClass.getSimpleName, id))
        )
      }
      .getOrElse(Map.empty)

    val maybeRelationsForOrg = Monad[Option]
      .map(ticket.organizationId) { orgId =>
        val id = Id(orgId.value)
        Map(
          SearchResult.OrganizationIdAtTicket ->
            DB.query[Organization](QueryParams(Searchable.Organizations, id.getClass.getSimpleName, id))
        )
      }
      .getOrElse(Map.empty)

    SearchResult(params, ticket: Searchable, relations ++ maybeRelationsForUser ++ maybeRelationsForOrg)
  }
}
