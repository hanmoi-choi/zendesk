package zendesk.model

import cats.syntax.either._
import io.circe._
import io.circe.syntax._
import org.specs2.mutable.Specification
import zendesk.helper.SampleDataGen

class OrganizationSpec extends Specification {

  "JSON string for a organization should be decoded to Organization object" >> {
    val result: Either[Error, Organization] = io.circe.parser.decode[Organization](SampleDataGen.rawOrganizationJson)

    "result should be expected Organization Object" >> {
      result must beEqualTo(SampleDataGen.organization.asRight)
    }
  }

  "A organization object should be encoded to Json String" >> {
    val result = SampleDataGen.organization.asJson.spaces2

    "result should be expected Organization Object" >> {
      result must beEqualTo(SampleDataGen.rawOrganizationJson)
    }
  }
}
