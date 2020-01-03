package zendesk.util

import cats.implicits._
import zendesk.model.{Database, Organization, Ticket, User}

case class DataFiles(
  var usersData: String = "./data/users.json",
  var organizationsData: String = "./data/organizations.json",
  var ticketsData: String = "./data/tickets.json"
)

case class DataBaseGenerator() {
  private val defaultUsersData = "./data/users.json"
  private val defaultOrganizationsData = "./data/organizations.json"
  private val defaultTicketsData = "./data/tickets.json"

  def generateDatabaseWithProgramArguments(args: List[String]): Option[Database] = {
    val dataFiles: Option[DataFiles] = args match {
      case List() =>
        Console.println(s"Files for Users data: $defaultUsersData")
        Console.println(s"Files for Organizations data: $defaultOrganizationsData")
        Console.println(s"Files for Tickets data: $defaultTicketsData")

        DataFiles().some
      case usersData :: Nil =>
        Console.println(s"Files for Users data: $usersData")
        Console.println(s"Files for Organizations data: $defaultOrganizationsData")
        Console.println(s"Files for Tickets data: $defaultTicketsData")

        DataFiles(usersData = usersData).some
      case usersData :: organizationsData :: Nil =>
        Console.println(s"Files for Users data: $usersData")
        Console.println(s"Files for Organizations data: $organizationsData")
        Console.println(s"Files for Tickets data: $defaultTicketsData")

        DataFiles(usersData = usersData, organizationsData = organizationsData).some
      case usersData :: organizationsData :: ticketsData :: Nil =>
        Console.println(s"Files for Users data: $usersData")
        Console.println(s"Files for Organizations data: $organizationsData")
        Console.println(s"Files for Tickets data: $ticketsData")

        DataFiles(usersData = usersData, organizationsData = organizationsData, ticketsData = ticketsData).some
      case _ =>
        Console.println(s"Invalid the number of argument is provided")
        None
    }

    val users: Option[Vector[User]] = dataFiles.map { df =>
      DataFileReader.getDataFromFile[User](df.usersData)
    }

    val tickets: Option[Vector[Ticket]] = dataFiles.map { df =>
      DataFileReader.getDataFromFile[Ticket](df.ticketsData)
    }

    val organizations: Option[Vector[Organization]] = dataFiles.map { df =>
      DataFileReader.getDataFromFile[Organization](df.organizationsData)
    }

    (users, tickets, organizations).mapN((u, t, o) => Database(userData = u, organizationData = o, ticketData = t))
  }
}
