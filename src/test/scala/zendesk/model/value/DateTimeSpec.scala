package zendesk.model.value

import java.time.ZonedDateTime

import org.specs2.mutable.Specification

class DateTimeSpec extends Specification {

  "DateTime format" >> {
    /*
        "last_login_at": "2013-08-04T01:03:27 -10:00",
     */
    "toString() should return malformed ISO8601 format to comply with the provided json files" >> {
      val dateTime = DateTime(ZonedDateTime.parse("2016-05-21T11:10:28-10:00"))

      dateTime.toString must beEqualTo("2016-05-21T11:10:28 -10:00")
    }
  }
}
