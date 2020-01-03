package zendesk.service.parser

import cats.syntax.either._
import org.specs2.ScalaCheck
import org.specs2.mutable.Specification
import zendesk.model.ParseFailure
import zendesk.service.parser.TermsToSearchUsers._
import zendesk.service.parser.TermsToSearchUsersParser.doParse

class TermsToSearchUsersParserSpec extends Specification with ScalaCheck {
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

  "should parse 'alias' as 'Alias' term" >> {
    "as lowercase" >> {
      doParse("alias") must beEqualTo(Alias.asRight)
    }
    "as uppercase" >> {
      doParse("ALIAS") must beEqualTo(Alias.asRight)
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

  "should parse 'active' as 'Active' term" >> {
    "as lowercase" >> {
      doParse("active") must beEqualTo(Active.asRight)
    }
    "as uppercase" >> {
      doParse("ACTIVE") must beEqualTo(Active.asRight)
    }

  }

  "should parse 'verified' as 'Verified' term" >> {
    "as lowercase" >> {
      doParse("verified") must beEqualTo(Verified.asRight)
    }
    "as uppercase" >> {
      doParse("VERIFIED") must beEqualTo(Verified.asRight)
    }
  }

  "should parse 'shared' as 'Shared' term" >> {
    "as lowercase" >> {
      doParse("shared") must beEqualTo(Shared.asRight)
    }
    "as uppercase" >> {
      doParse("SHARED") must beEqualTo(Shared.asRight)
    }
  }

  "should parse 'locale' as 'Locale' term" >> {
    "as lowercase" >> {
      doParse("locale") must beEqualTo(Locale.asRight)
    }
    "as uppercase" >> {
      doParse("LOCALE") must beEqualTo(Locale.asRight)
    }
  }

  "should parse 'timezone' as 'Timezone' term" >> {
    "as lowercase" >> {
      doParse("timezone") must beEqualTo(Timezone.asRight)
    }
    "as uppercase" >> {
      doParse("TIMEZONE") must beEqualTo(Timezone.asRight)
    }
  }

  "should parse 'lastLoginAt' as 'LastLoginAt' term" >> {
    "as lowercase" >> {
      doParse("lastLoginAt") must beEqualTo(LastLoginAt.asRight)
    }
    "as uppercase" >> {
      doParse("LASTLOGINAT") must beEqualTo(LastLoginAt.asRight)
    }
  }

  "should parse 'email' as 'Email' term" >> {
    "as lowercase" >> {
      doParse("email") must beEqualTo(Email.asRight)
    }
    "as uppercase" >> {
      doParse("EMAIL") must beEqualTo(Email.asRight)
    }
  }

  "should parse 'phone' as 'Phone' term" >> {
    "as lowercase" >> {
      doParse("phone") must beEqualTo(Phone.asRight)
    }
    "as uppercase" >> {
      doParse("PHONE") must beEqualTo(Phone.asRight)
    }
  }

  "should parse 'signature' as 'Signature' term" >> {
    "as lowercase" >> {
      doParse("signature") must beEqualTo(Signature.asRight)
    }
    "as uppercase" >> {
      doParse("SIGNATURE") must beEqualTo(Signature.asRight)
    }
  }

  "should parse 'organizationId' as 'OrganizationId' term" >> {
    "as lowercase" >> {
      doParse("organizationId") must beEqualTo(OrganizationId.asRight)
    }
    "as uppercase" >> {
      doParse("ORGANIZATIONID") must beEqualTo(OrganizationId.asRight)
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

  "should parse 'suspended' as 'Suspended' term" >> {
    "as lowercase" >> {
      doParse("suspended") must beEqualTo(Suspended.asRight)
    }
    "as uppercase" >> {
      doParse("SUSPENDED") must beEqualTo(Suspended.asRight)
    }
  }

  "should parse 'role' as 'Role' term" >> {
    "as lowercase" >> {
      doParse("role") must beEqualTo(Role.asRight)
    }
    "as uppercase" >> {
      doParse("ROLE") must beEqualTo(Role.asRight)
    }
  }

  "any other string inputs" >> prop { invalidCommand: String =>
    (invalidCommand != "id" &&
    invalidCommand.toLowerCase != "url" &&
    invalidCommand.toLowerCase != "externalId" &&
    invalidCommand.toLowerCase != "name" &&
    invalidCommand.toLowerCase != "alias" &&
    invalidCommand.toLowerCase != "createdAt" &&
    invalidCommand.toLowerCase != "active" &&
    invalidCommand.toLowerCase != "verified" &&
    invalidCommand.toLowerCase != "shared" &&
    invalidCommand.toLowerCase != "locale" &&
    invalidCommand.toLowerCase != "timezone" &&
    invalidCommand.toLowerCase != "lastLoginAt" &&
    invalidCommand.toLowerCase != "email" &&
    invalidCommand.toLowerCase != "phone" &&
    invalidCommand.toLowerCase != "signature" &&
    invalidCommand.toLowerCase != "organizationId" &&
    invalidCommand.toLowerCase != "tags" &&
    invalidCommand.toLowerCase != "suspended" &&
    invalidCommand.toLowerCase != "role") ==> prop { _: String =>
      val result = doParse(invalidCommand)
      val expectedError = ParseFailure(s"Cannot parse $invalidCommand as SearchUsersTerm").asLeft

      result must beEqualTo(expectedError)
    }
  }.set(minTestsOk = 50, workers = 3)

}
