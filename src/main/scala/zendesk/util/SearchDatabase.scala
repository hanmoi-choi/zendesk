package zendesk.util

import zendesk.model.value._
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

    table.getOrElse(queryParams.searchTerm.toLowerCase, Map.empty)
      .getOrElse(queryParams.searchValue, Vector.empty)
      .asInstanceOf[Vector[T]]
  }

  private val userTable: MMap[SearchTerm, Map[SearchValue, Vector[User]]] = MMap()

  private val organizationTable: MMap[SearchTerm, Map[SearchValue, Vector[Organization]]] = MMap()

  private val ticketTable: MMap[SearchTerm, Map[SearchValue, Vector[Ticket]]] = MMap()

  private def groupAndExtractValues[T <: SearchValue, V <: Searchable](
                                                                        data: Vector[(T, V)]
                                                                      ): Map[T, Vector[V]] = {
    data.groupBy(_._1).view.mapValues(_.map(_._2)).toMap
  }

  def createUsersTable(data: Vector[User]): Unit = {
    val usersGroupedByTag: Map[SearchValue, Vector[User]] = groupAndExtractValues(data.flatMap(_.pairWithTag()))

    userTable.put("id".toLowerCase, data.groupBy(_.id))
    userTable.put("url".toLowerCase, data.groupBy(_.url))
    userTable.put("externalId".toLowerCase, data.groupBy(_.externalId))
    userTable.put("name".toLowerCase, data.groupBy(_.name))
    userTable.put("alias".toLowerCase, data.groupBy(_.alias.getOrElse(EmptyStringSearchField)))
    userTable.put("createdAt".toLowerCase, data.groupBy(_.createdAt))
    userTable.put("active".toLowerCase, data.groupBy(_.active))
    userTable.put("verified".toLowerCase, data.groupBy(_.verified.getOrElse(EmptyStringSearchField)))
    userTable.put("shared".toLowerCase, data.groupBy(_.shared))
    userTable.put("locale".toLowerCase, data.groupBy(_.locale.getOrElse(EmptyStringSearchField)))
    userTable.put("timezone".toLowerCase, data.groupBy(_.timezone.getOrElse(EmptyStringSearchField)))
    userTable.put("lastLoginAt".toLowerCase, data.groupBy(_.lastLoginAt))
    userTable.put("email".toLowerCase, data.groupBy(_.email.getOrElse(EmptyStringSearchField)))
    userTable.put("phone".toLowerCase, data.groupBy(_.phone))
    userTable.put("signature".toLowerCase, data.groupBy(_.signature))
    userTable.put("organizationId".toLowerCase, data.groupBy(_.organizationId.getOrElse(EmptyStringSearchField)))
    userTable.put("tags".toLowerCase, usersGroupedByTag)
    userTable.put("suspended".toLowerCase, data.groupBy(_.suspended))
    userTable.put("role".toLowerCase, data.groupBy(_.role))
  }

  def createOrganizationsTable(data: Vector[Organization]): Unit = {
    val orgsGroupedByTag: Map[SearchValue, Vector[Organization]] = groupAndExtractValues(data.flatMap(_.pairWithTag()))
    val orgsGroupedByDomainName: Map[SearchValue, Vector[Organization]] = groupAndExtractValues(data.flatMap(_.pairWithDomainName()))

    organizationTable.put("id".toLowerCase, data.groupBy(_.id))
    organizationTable.put("url".toLowerCase, data.groupBy(_.url))
    organizationTable.put("externalId".toLowerCase, data.groupBy(_.externalId))
    organizationTable.put("name".toLowerCase, data.groupBy(_.name))
    organizationTable.put("createdAt".toLowerCase, data.groupBy(_.createdAt))
    organizationTable.put("details".toLowerCase, data.groupBy(_.details))
    organizationTable.put("sharedTickets".toLowerCase, data.groupBy(_.sharedTickets))
    organizationTable.put("tags".toLowerCase, orgsGroupedByTag)
    organizationTable.put("domainNames".toLowerCase, orgsGroupedByDomainName)
  }

  def createTicketTable(data: Vector[Ticket]): Unit = {
    val ticketsGroupedByTag: Map[SearchValue, Vector[Ticket]] = groupAndExtractValues(data.flatMap(_.pairWithTag()))

    ticketTable.put("id".toLowerCase, data.groupBy(_.id))
    ticketTable.put("url".toLowerCase, data.groupBy(_.url))
    ticketTable.put("externalId".toLowerCase, data.groupBy(_.externalId))
    ticketTable.put("createdAt".toLowerCase, data.groupBy(_.createdAt))
    ticketTable.put("type".toLowerCase, data.groupBy(_.`type`.getOrElse(EmptyStringSearchField)))
    ticketTable.put("subject".toLowerCase, data.groupBy(_.subject))
    ticketTable.put("description".toLowerCase, data.groupBy(_.description.getOrElse(EmptyStringSearchField)))
    ticketTable.put("priority".toLowerCase, data.groupBy(_.priority))
    ticketTable.put("status".toLowerCase, data.groupBy(_.status))
    ticketTable.put("submitterId".toLowerCase, data.groupBy(_.submitterId))
    ticketTable.put("assigneeId".toLowerCase, data.groupBy(_.assigneeId.getOrElse(EmptyStringSearchField)))
    ticketTable.put("organizationId".toLowerCase, data.groupBy(_.organizationId.getOrElse(EmptyStringSearchField)))
    ticketTable.put("tags".toLowerCase, ticketsGroupedByTag)
    ticketTable.put("hasIncidents".toLowerCase, data.groupBy(_.hasIncidents))
    ticketTable.put("dueAt".toLowerCase, data.groupBy(_.dueAt.getOrElse(EmptyStringSearchField)))
    ticketTable.put("via".toLowerCase, data.groupBy(_.via))
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

