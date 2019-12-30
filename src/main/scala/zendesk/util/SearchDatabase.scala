package zendesk.util

import zendesk.model.value.{EmptyStringSearchField, QueryParams, SearchObject, SearchValue}
import zendesk.model.{Organization, Searchable, Ticket, User}

import scala.collection.mutable.{HashMap => MMap}

case class SearchDatabase() {
  type SearchTerm = String

  def query(queryParams: QueryParams): Vector[Searchable] = {
    val db = queryParams.searchObject match {
      case SearchObject.User => userDB
      case SearchObject.Ticket => ticketDB
      case SearchObject.Organization => organizationDB

    }

    db.getOrElse(queryParams.searchTerm, Map.empty)
      .getOrElse(queryParams.searchValue, Vector.empty)
  }

  private val userDB: MMap[SearchTerm, Map[SearchValue, Vector[User]]] = MMap()

  private val organizationDB: MMap[SearchTerm, Map[SearchValue, Vector[Organization]]] = MMap()

  private val ticketDB: MMap[SearchTerm, Map[SearchValue, Vector[Ticket]]] = MMap()

  def createUsersDB(data: Vector[User]): Unit = {
    userDB.put("id", data.groupBy(_.id))
    userDB.put("url", data.groupBy(_.url))
    userDB.put("externalId", data.groupBy(_.externalId))
    userDB.put("name", data.groupBy(_.name))
    userDB.put("alias", data.groupBy(_.alias.getOrElse(EmptyStringSearchField)))
    userDB.put("createdAt", data.groupBy(_.createdAt))
    userDB.put("active", data.groupBy(_.active))
    userDB.put("verified", data.groupBy(_.verified.getOrElse(EmptyStringSearchField)))
    userDB.put("shared", data.groupBy(_.shared))
    userDB.put("locale", data.groupBy(_.locale.getOrElse(EmptyStringSearchField)))
    userDB.put("timezone", data.groupBy(_.timezone.getOrElse(EmptyStringSearchField)))
    userDB.put("lastLoginAt", data.groupBy(_.lastLoginAt))
    userDB.put("email", data.groupBy(_.email.getOrElse(EmptyStringSearchField)))
    userDB.put("phone", data.groupBy(_.phone))
    userDB.put("signature", data.groupBy(_.signature))
    userDB.put("organizationId", data.groupBy(_.organizationId.getOrElse(EmptyStringSearchField)))
//    userDB.put("tags", data.groupBy(_.tags))
    userDB.put("suspended", data.groupBy(_.suspended))
    userDB.put("role", data.groupBy(_.role))
  }

  def createOrganizationsDB(data: Vector[Organization]): Unit = {
    organizationDB.put("id", data.groupBy(_.id))
    organizationDB.put("id", data.groupBy(_.id))
    organizationDB.put("id", data.groupBy(_.id))
    organizationDB.put("id", data.groupBy(_.id))
    organizationDB.put("id", data.groupBy(_.id))
    organizationDB.put("id", data.groupBy(_.id))
  }
}

