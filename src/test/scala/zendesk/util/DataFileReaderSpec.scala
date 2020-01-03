package zendesk.util

import org.specs2.mutable.Specification
import zendesk.model.User

class DataFileReaderSpec extends Specification {

  "DataFileReader" should {

    "Valid files" >> {
      "User" >> {
        val result = DataFileReader.getDataFromFile[User]("./data/users.json")

        result must beRight
      }
    }

  }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme
