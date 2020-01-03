package zendesk.model

import cats.syntax.either._
import io.circe._
import io.circe.syntax._
import org.specs2.mutable.Specification
import zendesk.helper.TestDataFactory

class OrganizationSpec extends Specification {
  "Simple Data String" >> {
    val expected =
      """
        |  organization_name : Enthaze""".stripMargin

    TestDataFactory.organization.asSimpleDataString must beEqualTo(expected)
  }

  "JSON string for a organization should be decoded to Organization object" >> {
    val result: Either[Error, Organization] = io.circe.parser.decode[Organization](TestDataFactory.rawOrganizationJson)

    "result should be expected Organization Object" >> {
      result must beEqualTo(TestDataFactory.organization.asRight)
    }
  }

  "A organization object should be encoded to Json String" >> {
    val result = TestDataFactory.organization.asJson.spaces2

    "result should be expected Organization Object" >> {
      result must beEqualTo(TestDataFactory.rawOrganizationJson)
    }
  }
}
