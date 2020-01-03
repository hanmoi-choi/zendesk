package zendesk.util

import cats.syntax.either._
import org.specs2.mutable.Specification
import zendesk.model.{FileNotExistError, JsonParseFailure, Organization, Ticket, User}

class DataFileReaderSpec extends Specification {

  "DataFileReader, get data from json files" should {
    "File does not exist" >> {
      "Users" >> {
        val result = DataFileReader.getDataFromFile[User]("./data/1.json")
        val expectedError = FileNotExistError("File('./data/1.json') does not exist")

        result must beEqualTo(expectedError.asLeft)
      }
    }
    "Invalid file is provided" >> {
      "Users" >> {
        val result = DataFileReader.getDataFromFile[Ticket]("./data/users.json")
        val expectedError = JsonParseFailure("DecodingFailure(String, List(DownField(_id), DownArray))")

        result must beEqualTo(expectedError.asLeft)
      }
    }

    "Valid files" >> {
      "Users" >> {
        val result = DataFileReader.getDataFromFile[User]("./data/users.json")

        result must beRight
      }

      "Tickets" >> {
        val result = DataFileReader.getDataFromFile[Ticket]("./data/tickets.json")

        result must beRight
      }

      "Organizations" >> {
        val result = DataFileReader.getDataFromFile[Organization]("./data/organizations.json")

        result must beRight
      }
    }

  }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme
