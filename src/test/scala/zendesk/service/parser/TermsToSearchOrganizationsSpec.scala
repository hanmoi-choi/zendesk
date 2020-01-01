package zendesk.service.parser

import java.util.UUID

import cats.syntax.either._
import org.joda.time.DateTime
import org.specs2.mutable.Specification
import zendesk.model.InvalidArgumentError
import zendesk.service.parser.TermsToSearchOrganizations._

//createdAt: ZenDateTime,
//sharedTickets: SharedTickets,

class TermsToSearchOrganizationsSpec extends Specification {
  private val emptyStringError = InvalidArgumentError("Empty string is not allowed for this term").asLeft

  "Convert string value to SearchValue object" >> {
    "when any non-empty string value is acceptable" >> {
      "Tag" >> {
        "empty string is not allowed" >> {
          Tags.asSearchValue("") must beEqualTo(emptyStringError)
        }

        "any string is allowed" >> {
          Tags.asSearchValue("foobar") must beEqualTo(zendesk.model.value.Tag("foobar").asRight)
        }
      }

      "Details" >> {
        "empty string is not allowed" >> {
          Details.asSearchValue("") must beEqualTo(emptyStringError)
        }

        "any string is allowed" >> {
          Details.asSearchValue("foobar") must beEqualTo(zendesk.model.value.Details("foobar").asRight)
        }
      }

      "Url" >> {
        "empty string is not allowed" >> {
          Url.asSearchValue("") must beEqualTo(emptyStringError)
        }

        "any string is allowed" >> {
          Url.asSearchValue("foobar") must beEqualTo(zendesk.model.value.Url("foobar").asRight)
        }
      }

      "DomainName" >> {
        "empty string is not allowed" >> {
          DomainNames.asSearchValue("") must beEqualTo(emptyStringError)
        }

        "any string is allowed" >> {
          DomainNames.asSearchValue("foobar") must beEqualTo(zendesk.model.value.DomainName("foobar").asRight)
        }
      }

      "Name" >> {
        "empty string is not allowed" >> {
          Name.asSearchValue("") must beEqualTo(emptyStringError)
        }

        "any string is allowed" >> {
          Name.asSearchValue("foobar") must beEqualTo(zendesk.model.value.Name("foobar").asRight)
        }
      }
    }

    "when string value has constraint to be parsable" >> {
      "Should be Integer value" >> {
        "when non-empty string value is acceptable" >> {
          "Id" >> {
            "empty string is not allowed" >> {
              Id.asSearchValue("") must beEqualTo(emptyStringError)
            }

            "Valid input, integer" >> {
              Id.asSearchValue("1") must beEqualTo(zendesk.model.value.Id(1).asRight)
            }

            "Invalid input, non-integer" >> {
              Id.asSearchValue("a") must beEqualTo(InvalidArgumentError("'a' is not Integer value").asLeft)
            }
          }
        }
      }

      "Should be Boolean value" >> {
        "when non-empty string value is acceptable" >> {
          "SharedTickets" >> {
            "empty string is not allowed" >> {
              SharedTickets.asSearchValue("") must beEqualTo(emptyStringError)
            }

            "Valid input, boolean" >> {
              SharedTickets.asSearchValue("true") must beEqualTo(zendesk.model.value.SharedTickets(true).asRight)
              SharedTickets.asSearchValue("false") must beEqualTo(zendesk.model.value.SharedTickets(false).asRight)
            }

            "Invalid input, non-boolean" >> {
              SharedTickets.asSearchValue("a") must beEqualTo(InvalidArgumentError("'a' is not Boolean value").asLeft)
            }
          }
        }
      }

      "Should be UUID value" >> {
        "when non-empty string value is acceptable" >> {
          "Role" >> {
            "empty string is not allowed" >> {
              ExternalId.asSearchValue("") must beEqualTo(emptyStringError)
            }

            "Valid input, uuid" >> {
              val uuid = UUID.randomUUID()
              ExternalId.asSearchValue(uuid.toString) must beEqualTo(zendesk.model.value.ExternalId(uuid).asRight)
            }

            "Invalid input, non-uuid" >> {
              ExternalId.asSearchValue("a") must beEqualTo(InvalidArgumentError("'a' is not UUID value").asLeft)
            }
          }
        }
      }

      "Should be DateTime value" >> {
        "CreatedAt" >> {
          "empty string is not allowed" >> {
            CreatedAt.asSearchValue("") must beEqualTo(emptyStringError)
          }

          "Valid input, datetime" >> {
            val dataTimeString = "2016-04-15T05:19:46 -10:00"
            val expectedDateTime = DateTime.parse("2016-04-15T05:19:46-10:00")
            CreatedAt.asSearchValue(dataTimeString) must beEqualTo(
              zendesk.model.value.ZenDateTime(expectedDateTime).asRight)
          }

          "Invalid input, non-datetime" >> {
            CreatedAt.asSearchValue("a") must beEqualTo(InvalidArgumentError("'a' is not DateTime value").asLeft)
          }
        }
      }
    }
  }
}
