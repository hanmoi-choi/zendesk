package zendesk.model.value

import cats.syntax.option._
import org.specs2.mutable.Specification

class RoleSpec extends Specification {
  "Role" should {
    "from String; case insensitive" in {
      "when string 'admin'" >> {
        Role.fromString("admin") must beEqualTo(Admin.some)
        Role.fromString("ADMIN") must beEqualTo(Admin.some)
      }

      "when string 'end-user'" >> {
        Role.fromString("end-user") must beEqualTo(EndUser.some)
        Role.fromString("END-USER") must beEqualTo(EndUser.some)
      }

      "when string 'agent'" >> {
        Role.fromString("agent") must beEqualTo(Agent.some)
        Role.fromString("AGENT") must beEqualTo(Agent.some)
      }

      "when string 'foobar'" >> {
        Role.fromString("foobar") must beNone
      }
    }
  }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme