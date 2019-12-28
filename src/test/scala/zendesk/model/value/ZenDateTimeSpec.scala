package zendesk.model.value

import org.joda.time.DateTime
import org.specs2.mutable.Specification

class ZenDateTimeSpec extends Specification {

  "DateTime format" >> {
    /*
        "last_login_at": "2013-08-04T01:03:27 -10:00",
     */
    "toString() should return malformed ISO8601 format to comply with the provided json files" >> {
      val dateTime = ZenDateTime(DateTime.parse("2016-05-21T11:10:28-10:00"))

      dateTime.toString must beEqualTo("2016-05-21T11:10:28 -10:00")
    }
  }
}
