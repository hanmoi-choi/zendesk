package zendesk.model.value

import cats.syntax.option._
import org.specs2.mutable.Specification

class PrioritySpec extends Specification {
  "Priority" should {
    "from String; case insensitive" in {
      "when string 'urgent'" >> {
        Priority.fromString("urgent") must beEqualTo(Urgent.some)
        Priority.fromString("URGENT") must beEqualTo(Urgent.some)
      }

      "when string 'high'" >> {
        Priority.fromString("high") must beEqualTo(High.some)
        Priority.fromString("HIGH") must beEqualTo(High.some)
      }

      "when string 'normal'" >> {
        Priority.fromString("normal") must beEqualTo(Normal.some)
        Priority.fromString("NORMAL") must beEqualTo(Normal.some)
      }

      "when string 'low'" >> {
        Priority.fromString("low") must beEqualTo(Low.some)
        Priority.fromString("LOW") must beEqualTo(Low.some)
      }

      "when string 'foobar'" >> {
        Priority.fromString("foobar") must beNone
      }
    }
  }

}
