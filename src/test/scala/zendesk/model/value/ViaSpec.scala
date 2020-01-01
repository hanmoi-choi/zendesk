package zendesk.model.value

import cats.syntax.option._
import org.specs2.mutable.Specification

class ViaSpec extends Specification {
  "Via" should {
    "from String; case insensitive" in {
      "when string 'web'" >> {
        Via.fromString("web") must beEqualTo(Web.some)
        Via.fromString("WEB") must beEqualTo(Web.some)
      }

      "when string 'voice'" >> {
        Via.fromString("voice") must beEqualTo(Voice.some)
        Via.fromString("VOICE") must beEqualTo(Voice.some)
      }

      "when string 'chat'" >> {
        Via.fromString("chat") must beEqualTo(Chat.some)
        Via.fromString("CHAT") must beEqualTo(Chat.some)
      }

      "when string 'foobar'" >> {
        Via.fromString("foobar") must beNone
      }
    }
  }
}
