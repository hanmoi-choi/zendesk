package zendesk.util

import cats.implicits._
import zendesk.model.{AppError, Database, InvalidArgumentError, Organization, Ticket, User}

case class DataFiles(
  var usersData: String = "./data/users.json",
  var organizationsData: String = "./data/organizations.json",
  var ticketsData: String = "./data/tickets.json"
)

case class DataBaseGenerator() {
  private val defaultUsersData = "./data/users.json"
  private val defaultOrganizationsData = "./data/organizations.json"
  private val defaultTicketsData = "./data/tickets.json"

  def generateDatabaseWithProgramArguments(args: List[String]): Either[AppError, Database] = {
    val dataFiles: Either[AppError, DataFiles] = args match {
      case List() =>
        Console.println(s"Files for Users data: $defaultUsersData")
        Console.println(s"Files for Organizations data: $defaultOrganizationsData")
        Console.println(s"Files for Tickets data: $defaultTicketsData")

        DataFiles().asRight
      case usersData :: Nil =>
        Console.println(s"Files for Users data: $usersData")
        Console.println(s"Files for Organizations data: $defaultOrganizationsData")
        Console.println(s"Files for Tickets data: $defaultTicketsData")

        DataFiles(usersData = usersData).asRight
      case usersData :: organizationsData :: Nil =>
        Console.println(s"Files for Users data: $usersData")
        Console.println(s"Files for Organizations data: $organizationsData")
        Console.println(s"Files for Tickets data: $defaultTicketsData")

        DataFiles(usersData = usersData, organizationsData = organizationsData).asRight
      case usersData :: organizationsData :: ticketsData :: Nil =>
        Console.println(s"Files for Users data: $usersData")
        Console.println(s"Files for Organizations data: $organizationsData")
        Console.println(s"Files for Tickets data: $ticketsData")

        DataFiles(usersData = usersData, organizationsData = organizationsData, ticketsData = ticketsData).asRight
      case _ =>
        Console.println(s"Invalid the number of argument is provided")
        InvalidArgumentError("Arguments for data files are allowed upto 3; users, organizations, tickets").asLeft
    }

    val users: Either[AppError, Vector[User]] = dataFiles.flatMap { df =>
      DataFileReader.getDataFromFile[User](df.usersData)
    }

    val tickets = dataFiles.flatMap { df =>
      DataFileReader.getDataFromFile[Ticket](df.ticketsData)
    }

    val organizations = dataFiles.flatMap { df =>
      DataFileReader.getDataFromFile[Organization](df.organizationsData)
    }

    (users, tickets, organizations).mapN((u, t, o) => Database(userData = u, organizationData = o, ticketData = t))
  }
}
