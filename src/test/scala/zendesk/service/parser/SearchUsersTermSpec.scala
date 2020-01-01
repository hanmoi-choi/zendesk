package zendesk.service.parser

import java.util.UUID

import cats.syntax.either._
import org.joda.time.DateTime
import org.specs2.mutable.Specification
import zendesk.model.InvalidArgumentError
import zendesk.model.value.EmptyStringSearchField
import zendesk.service.parser.SearchUsersTerm._


class SearchUsersTermSpec extends Specification {
  private val emptyStringError = InvalidArgumentError("Empty string is not allowed for this term").asLeft

  "Convert string value to SearchValue object" >> {
    "when any string value is acceptable include empty string" >> {
      "Alias" >> {
        "empty string is allowed but return as 'EmptyStringSearchField'" >> {
          Alias.asSearchValue("") must beEqualTo(EmptyStringSearchField.asRight)
        }

        "any string is allowed" >> {
          Alias.asSearchValue("foobar") must beEqualTo(zendesk.model.value.Alias("foobar").asRight)
        }
      }

      "Locale" >> {
        "empty string is allowed but return as 'EmptyStringSearchField'" >> {
          Locale.asSearchValue("") must beEqualTo(EmptyStringSearchField.asRight)
        }

        "any string is allowed" >> {
          Locale.asSearchValue("foobar") must beEqualTo(zendesk.model.value.Locale("foobar").asRight)
        }
      }

      "Timezone" >> {
        "empty string is allowed but return as 'EmptyStringSearchField'" >> {
          Timezone.asSearchValue("") must beEqualTo(EmptyStringSearchField.asRight)
        }

        "any string is allowed" >> {
          Timezone.asSearchValue("foobar") must beEqualTo(zendesk.model.value.Timezone("foobar").asRight)
        }
      }

      "Email" >> {
        "empty string is allowed but return as 'EmptyStringSearchField'" >> {
          Email.asSearchValue("") must beEqualTo(EmptyStringSearchField.asRight)
        }

        "any string is allowed" >> {
          Email.asSearchValue("foobar") must beEqualTo(zendesk.model.value.Email("foobar").asRight)
        }
      }
    }

    "when any non-empty string value is acceptable" >> {
      "Tag" >> {
        "empty string is not allowed" >> {
          Tags.asSearchValue("") must beEqualTo(emptyStringError)
        }

        "any string is allowed" >> {
          Tags.asSearchValue("foobar") must beEqualTo(zendesk.model.value.Tag("foobar").asRight)
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

      "Name" >> {
        "empty string is not allowed" >> {
          Name.asSearchValue("") must beEqualTo(emptyStringError)
        }

        "any string is allowed" >> {
          Name.asSearchValue("foobar") must beEqualTo(zendesk.model.value.Name("foobar").asRight)
        }
      }

      "Phone" >> {
        "empty string is not allowed" >> {
          Phone.asSearchValue("") must beEqualTo(emptyStringError)
        }

        "any string is allowed" >> {
          Phone.asSearchValue("foobar") must beEqualTo(zendesk.model.value.Phone("foobar").asRight)
        }
      }

      "Signature" >> {
        "empty string is not allowed" >> {
          Signature.asSearchValue("") must beEqualTo(emptyStringError)
        }

        "any string is allowed" >> {
          Signature.asSearchValue("foobar") must beEqualTo(zendesk.model.value.Signature("foobar").asRight)
        }
      }
    }

    "when string value has constraint to be parsable" >> {
      "Should be Integer value" >> {
        "when any string value is acceptable include empty string" >> {
          "OrganizationId" >> {
            "empty string is allowed but return as 'EmptyStringSearchField'" >> {
              OrganizationId.asSearchValue("") must beEqualTo(EmptyStringSearchField.asRight)
            }

            "Valid input, integer" >> {
              OrganizationId.asSearchValue("1") must beEqualTo(zendesk.model.value.Id(1).asRight)
            }

            "Invalid input, non-integer" >> {
              OrganizationId.asSearchValue("a") must beEqualTo(InvalidArgumentError("'a' is not Integer value").asLeft)
            }
          }
        }

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
        "when any string value is acceptable include empty string" >> {
          "Verified" >> {
            "empty string is allowed but return as 'EmptyStringSearchField'" >> {
              Verified.asSearchValue("") must beEqualTo(EmptyStringSearchField.asRight)
            }

            "Valid input, boolean" >> {
              Verified.asSearchValue("true") must beEqualTo(zendesk.model.value.Verified(true).asRight)
              Verified.asSearchValue("false") must beEqualTo(zendesk.model.value.Verified(false).asRight)
            }

            "Invalid input, non-boolean" >> {
              Verified.asSearchValue("a") must beEqualTo(InvalidArgumentError("'a' is not Boolean value").asLeft)
            }
          }
        }

        "when non-empty string value is acceptable" >> {
          "Shared" >> {
            "empty string is not allowed" >> {
              Shared.asSearchValue("") must beEqualTo(emptyStringError)
            }

            "Valid input, boolean" >> {
              Shared.asSearchValue("true") must beEqualTo(zendesk.model.value.Shared(true).asRight)
              Shared.asSearchValue("false") must beEqualTo(zendesk.model.value.Shared(false).asRight)
            }

            "Invalid input, non-boolean" >> {
              Shared.asSearchValue("a") must beEqualTo(InvalidArgumentError("'a' is not Boolean value").asLeft)
            }
          }

          "Active" >> {
            "empty string is not allowed" >> {
              Active.asSearchValue("") must beEqualTo(emptyStringError)
            }

            "Valid input, boolean" >> {
              Active.asSearchValue("true") must beEqualTo(zendesk.model.value.Active(true).asRight)
              Active.asSearchValue("false") must beEqualTo(zendesk.model.value.Active(false).asRight)
            }

            "Invalid input, non-boolean" >> {
              Active.asSearchValue("a") must beEqualTo(InvalidArgumentError("'a' is not Boolean value").asLeft)
            }
          }

          "Suspended" >> {
            "empty string is not allowed" >> {
              Suspended.asSearchValue("") must beEqualTo(emptyStringError)
            }

            "Valid input, boolean" >> {
              Suspended.asSearchValue("true") must beEqualTo(zendesk.model.value.Suspended(true).asRight)
              Suspended.asSearchValue("false") must beEqualTo(zendesk.model.value.Suspended(false).asRight)
            }

            "Invalid input, non-boolean" >> {
              Suspended.asSearchValue("a") must beEqualTo(InvalidArgumentError("'a' is not Boolean value").asLeft)
            }
          }
        }
      }

      "Should be Enum value" >> {
        "when non-empty string value is acceptable" >> {
          "Role" >> {
            "empty string is not allowed" >> {
              Role.asSearchValue("") must beEqualTo(emptyStringError)
            }

            "Valid input, boolean" >> {
              Role.asSearchValue("admin") must beEqualTo(zendesk.model.value.Admin.asRight)
              Role.asSearchValue("agent") must beEqualTo(zendesk.model.value.Agent.asRight)
              Role.asSearchValue("end-user") must beEqualTo(zendesk.model.value.EndUser.asRight)
            }

            "Invalid input, non-boolean" >> {
              Role.asSearchValue("a") must beEqualTo(InvalidArgumentError("'a' is not Role('admin', 'agent', 'end-user') value").asLeft)
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
        "when non-empty string value is acceptable" >> {
          "CreatedAt" >> {
            "empty string is not allowed" >> {
              CreatedAt.asSearchValue("") must beEqualTo(emptyStringError)
            }

            "Valid input, datetime" >> {
              val dataTimeString = "2016-04-15T05:19:46 -10:00"
              val expectedDateTime = DateTime.parse("2016-04-15T05:19:46-10:00")
              CreatedAt.asSearchValue(dataTimeString) must beEqualTo(zendesk.model.value.ZenDateTime(expectedDateTime).asRight)
            }

            "Invalid input, non-datetime" >> {
              CreatedAt.asSearchValue("a") must beEqualTo(InvalidArgumentError("'a' is not DateTime value").asLeft)
            }
          }
        }
      }

      "Should be Enum value" >> {
        "when non-empty string value is acceptable" >> {
          "Role" >> {
            "empty string is not allowed" >> {
              Role.asSearchValue("") must beEqualTo(emptyStringError)
            }

            "Valid input, enum" >> {
              Role.asSearchValue("admin") must beEqualTo(zendesk.model.value.Admin.asRight)
              Role.asSearchValue("agent") must beEqualTo(zendesk.model.value.Agent.asRight)
              Role.asSearchValue("end-user") must beEqualTo(zendesk.model.value.EndUser.asRight)
            }

            "Invalid input, non-enum" >> {
              Role.asSearchValue("a") must beEqualTo(InvalidArgumentError("'a' is not Role('admin', 'agent', 'end-user') value").asLeft)
            }
          }
        }
      }
    }
  }
}
