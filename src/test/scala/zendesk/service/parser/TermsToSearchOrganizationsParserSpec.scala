package zendesk.service.parser

import cats.syntax.either._
import org.specs2.ScalaCheck
import org.specs2.mutable.Specification
import zendesk.model.ParseFailure
import zendesk.service.parser.TermsToSearchOrganizations._
import zendesk.service.parser.TermsToSearchOrganizationsParser.doParse

import scala.language.postfixOps

class TermsToSearchOrganizationsParserSpec extends Specification with ScalaCheck {
  "should parse 'id' as 'Id' term" >> {
    "as lowercase" >> {
      doParse("id") must beEqualTo(Id.asRight)
    }
    "as uppercase" >> {
      doParse("ID") must beEqualTo(Id.asRight)
    }
  }

  "should parse 'url' as 'Url' term" >> {
    "as lowercase" >> {
      doParse("url") must beEqualTo(Url.asRight)
    }
    "as uppercase" >> {
      doParse("URL") must beEqualTo(Url.asRight)
    }
  }

  "should parse 'externalId' as 'ExternalId' term" >> {
    "as lowercase" >> {
      doParse("externalId") must beEqualTo(ExternalId.asRight)
    }
    "as uppercase" >> {
      doParse("EXTERNALID") must beEqualTo(ExternalId.asRight)
    }
  }

  "should parse 'name' as 'Name' term" >> {
    "as lowercase" >> {
      doParse("name") must beEqualTo(Name.asRight)
    }
    "as uppercase" >> {
      doParse("NAME") must beEqualTo(Name.asRight)
    }
  }

  "should parse 'domainNames' as 'DomainNames' term" >> {
    "as lowercase" >> {
      doParse("domainNames") must beEqualTo(DomainNames.asRight)
    }
    "as uppercase" >> {
      doParse("DOMAINNAMES") must beEqualTo(DomainNames.asRight)
    }
  }

  "should parse 'createdAt' as 'CreatedAt' term" >> {
    "as lowercase" >> {
      doParse("createdAt") must beEqualTo(CreatedAt.asRight)
    }
    "as uppercase" >> {
      doParse("CREATEDAT") must beEqualTo(CreatedAt.asRight)
    }
  }

  "should parse 'tags' as 'Tags' term" >> {
    "as lowercase" >> {
      doParse("tags") must beEqualTo(Tags.asRight)
    }
    "as uppercase" >> {
      doParse("TAGS") must beEqualTo(Tags.asRight)
    }
  }

  "should parse 'details' as 'Details' term" >> {
    "as lowercase" >> {
      doParse("details") must beEqualTo(Details.asRight)
    }
    "as uppercase" >> {
      doParse("DETAILS") must beEqualTo(Details.asRight)
    }
  }

  "should parse 'sharedTickets' as 'SharedTickets' term" >> {
    "as lowercase" >> {
      doParse("sharedTickets") must beEqualTo(SharedTickets.asRight)
    }
    "as uppercase" >> {
      doParse("SHAREDTICKETS") must beEqualTo(SharedTickets.asRight)
    }
  }

  "Should parse 'quit' as 'Quit' command" >> {
    "as lowercase" >> {
      doParse("quit") must beEqualTo(Quit.asRight)
    }

    "as uppercase" >> {
      doParse("QUIT") must beEqualTo(Quit.asRight)
    }
  }

  "any other string inputs" >> prop { invalidCommand: String =>
    (invalidCommand != "id" &&
    invalidCommand.toLowerCase != "url" &&
    invalidCommand.toLowerCase != "externalId" &&
    invalidCommand.toLowerCase != "name" &&
    invalidCommand.toLowerCase != "createdAt" &&
    invalidCommand.toLowerCase != "tags" &&
    invalidCommand.toLowerCase != "domainNames" &&
    invalidCommand.toLowerCase != "details" &&
    invalidCommand.toLowerCase != "sharedTickets" &&
    invalidCommand.toLowerCase != "quit") ==> prop { _: String =>
      val result = doParse(invalidCommand)
      val expectedError = ParseFailure(s"Cannot parse $invalidCommand as SearchOrganizationsTerm").asLeft

      result must beEqualTo(expectedError)
    }
  }.set(minTestsOk = 50, workers = 3)

}
