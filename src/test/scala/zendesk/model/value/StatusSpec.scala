package zendesk.model.value

import cats.syntax.option._
import org.specs2.mutable.Specification

class StatusSpec extends Specification {

  "Status" should {
    "from String; case insensitive" in {
      "when string 'closed'" >> {
        Status.fromString("closed") must beEqualTo(Closed.some)
        Status.fromString("CLOSED") must beEqualTo(Closed.some)
      }

      "when string 'hold'" >> {
        Status.fromString("hold") must beEqualTo(Hold.some)
        Status.fromString("HOLD") must beEqualTo(Hold.some)
      }

      "when string 'open'" >> {
        Status.fromString("open") must beEqualTo(Open.some)
        Status.fromString("OPEN") must beEqualTo(Open.some)
      }

      "when string 'pending'" >> {
        Status.fromString("pending") must beEqualTo(Pending.some)
        Status.fromString("PENDING") must beEqualTo(Pending.some)
      }

      "when string 'solved'" >> {
        Status.fromString("solved") must beEqualTo(Solved.some)
        Status.fromString("SOLVED") must beEqualTo(Solved.some)
      }

      "when string 'foobar'" >> {
        Status.fromString("foobar") must beNone
      }
    }
  }

}

