package zendesk.model

import io.circe.Decoder
import io.circe.parser.decode
import io.circe.syntax._
import org.specs2.ScalaCheck
import org.specs2.mutable.Specification
import zendesk.helper.PropertyTestDataFactory.{orgArbitrary, ticketArbitrary, userArbitrary}

class EncodeAndDecodeCircularSpec extends Specification with ScalaCheck {

  "Organisation object should be able to do circular JSON Encode/Decode" >> prop { (organization: Organization) =>
    val result = decodeJson[Organization](organization.asJson.noSpaces, Organization.decodeOrg)

    organization must beEqualTo(result.get)
  }.set(minTestsOk = 100, workers = 3)

  "User object should be able to do circular JSON Encode/Decode" >> prop { (user: User) =>
    val result = decodeJson[User](user.asJson.noSpaces, User.decodeUser)

    user must beEqualTo(result.get)
  }.set(minTestsOk = 100, workers = 3)

  "Ticket object should be able to do circular JSON Encode/Decode" >> prop { (ticket: Ticket) =>
    val result = decodeJson[Ticket](ticket.asJson.noSpaces, Ticket.decodeTicket)

    ticket must beEqualTo(result.get)
  }.set(minTestsOk = 100, workers = 3)

  private def decodeJson[T](rawJson: String, decoder: Decoder[T]): Option[T] =
    decode[T](rawJson)(decoder).toOption

}
