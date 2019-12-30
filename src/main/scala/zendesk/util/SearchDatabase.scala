package zendesk.util

import zendesk.model.value.{EmptyStringSearchField, QueryParams, SearchObject, SearchValue}
import zendesk.model.{Organization, Searchable, Ticket, User}

import scala.collection.mutable.{HashMap => MMap}

case class SearchDatabase() {

  type SearchTerm = String

  def query[T](queryParams: QueryParams): Vector[T] = {
    val table = queryParams.searchObject match {
      case SearchObject.User => userTable
      case SearchObject.Ticket => ticketTable
      case SearchObject.Organization => organizationTable

    }

    table.getOrElse(queryParams.searchTerm, Map.empty)
      .getOrElse(queryParams.searchValue, Vector.empty)
      .asInstanceOf[Vector[T]]
  }

  private val userTable: MMap[SearchTerm, Map[SearchValue, Vector[User]]] = MMap()

  private val organizationTable: MMap[SearchTerm, Map[SearchValue, Vector[Organization]]] = MMap()

  private val ticketTable: MMap[SearchTerm, Map[SearchValue, Vector[Ticket]]] = MMap()

  def createUsersTable(data: Vector[User]): Unit = {
    userTable.put("id", data.groupBy(_.id))
    userTable.put("url", data.groupBy(_.url))
    userTable.put("externalId", data.groupBy(_.externalId))
    userTable.put("name", data.groupBy(_.name))
    userTable.put("alias", data.groupBy(_.alias.getOrElse(EmptyStringSearchField)))
    userTable.put("createdAt", data.groupBy(_.createdAt))
    userTable.put("active", data.groupBy(_.active))
    userTable.put("verified", data.groupBy(_.verified.getOrElse(EmptyStringSearchField)))
    userTable.put("shared", data.groupBy(_.shared))
    userTable.put("locale", data.groupBy(_.locale.getOrElse(EmptyStringSearchField)))
    userTable.put("timezone", data.groupBy(_.timezone.getOrElse(EmptyStringSearchField)))
    userTable.put("lastLoginAt", data.groupBy(_.lastLoginAt))
    userTable.put("email", data.groupBy(_.email.getOrElse(EmptyStringSearchField)))
    userTable.put("phone", data.groupBy(_.phone))
    userTable.put("signature", data.groupBy(_.signature))
    userTable.put("organizationId", data.groupBy(_.organizationId.getOrElse(EmptyStringSearchField)))
//    userTable.put("tags", data.groupBy(_.tags))
    userTable.put("suspended", data.groupBy(_.suspended))
    userTable.put("role", data.groupBy(_.role))
  }

  def createOrganizationsTable(data: Vector[Organization]): Unit = {
    organizationTable.put("id", data.groupBy(_.id))
    organizationTable.put("url", data.groupBy(_.url))
    organizationTable.put("externalId", data.groupBy(_.externalId))
    organizationTable.put("name", data.groupBy(_.name))
    organizationTable.put("createdAt", data.groupBy(_.createdAt))
    organizationTable.put("details", data.groupBy(_.details))
    organizationTable.put("sharedTickets", data.groupBy(_.sharedTickets))

    //    organizationTable.put("tags", data.groupBy(_.id))
    //    organizationTable.put("domainNames", data.groupBy(_.id))
  }

  def createTicketTable(data: Vector[Ticket]): Unit = {
    ticketTable.put("id", data.groupBy(_.id))
    ticketTable.put("url", data.groupBy(_.url))
    ticketTable.put("externalId", data.groupBy(_.externalId))
    ticketTable.put("createdAt", data.groupBy(_.createdAt))
    ticketTable.put("type", data.groupBy(_.`type`.getOrElse(EmptyStringSearchField)))
    ticketTable.put("subject", data.groupBy(_.subject))
    ticketTable.put("description", data.groupBy(_.description.getOrElse(EmptyStringSearchField)))
    ticketTable.put("priority", data.groupBy(_.priority))
    ticketTable.put("status", data.groupBy(_.status))
    ticketTable.put("submitterId", data.groupBy(_.submitterId))
    ticketTable.put("assigneeId", data.groupBy(_.assigneeId.getOrElse(EmptyStringSearchField)))
    ticketTable.put("organizationId", data.groupBy(_.organizationId.getOrElse(EmptyStringSearchField)))
//    ticketTable.put("tags", data.groupBy(_.tags))
    ticketTable.put("hasIncidents", data.groupBy(_.hasIncidents))
    ticketTable.put("dueAt", data.groupBy(_.dueAt.getOrElse(EmptyStringSearchField)))
    ticketTable.put("via", data.groupBy(_.via))
  }
}

object SearchDatabase {
  def apply(
           userData: Vector[User] = Vector.empty,
           organizationData: Vector[Organization] = Vector.empty,
           ticketData: Vector[Ticket] = Vector.empty
           ): SearchDatabase = {
    val db = SearchDatabase()
    db.createUsersTable(userData)
    db.createOrganizationsTable(organizationData)
    db.createTicketTable(ticketData)

    db
  }
}

