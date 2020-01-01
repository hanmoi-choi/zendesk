package zendesk.service.parser

import java.util.UUID

import cats.syntax.either._
import org.joda.time.DateTime
import org.specs2.mutable.Specification
import zendesk.model.InvalidArgumentError
import zendesk.model.value.EmptyStringSearchField
import zendesk.service.parser.TermsToSearchTickets._
import zendesk.service.parser.TermsToSearchUsers.{CreatedAt, ExternalId}

class TermsToSearchTicketsSpec extends Specification {
  private val emptyStringError = InvalidArgumentError("Empty string is not allowed for this term").asLeft

  "Convert string value to SearchValue object" >> {
    "when any string value is acceptable include empty string" >> {
      "Description" >> {
        "empty string is allowed but return as 'EmptyStringSearchField'" >> {
          Description.asSearchValue("") must beEqualTo(EmptyStringSearchField.asRight)
        }

        "any string is allowed" >> {
          Description.asSearchValue("foobar") must beEqualTo(zendesk.model.value.Description("foobar").asRight)
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

      "Subject" >> {
        "empty string is not allowed" >> {
          Subject.asSearchValue("") must beEqualTo(emptyStringError)
        }

        "any string is allowed" >> {
          Subject.asSearchValue("foobar") must beEqualTo(zendesk.model.value.Subject("foobar").asRight)
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
    }

    "when string value has constraint to be parsable" >> {
      "Should be Integer value" >> {
        "when any string value is acceptable include empty string" >> {
          "OrganizationId" >> {
            "empty string is allowed but return as 'EmptyStringSearchField'" >> {
              OrganizationId.asSearchValue("") must beEqualTo(EmptyStringSearchField.asRight)
            }

            "Valid input, integer" >> {
              OrganizationId.asSearchValue("1") must beEqualTo(zendesk.model.value.OrganizationId(1).asRight)
            }

            "Invalid input, non-integer" >> {
              OrganizationId.asSearchValue("a") must beEqualTo(InvalidArgumentError("'a' is not Integer value").asLeft)
            }
          }

          "AssigneeId" >> {
            "empty string is allowed but return as 'EmptyStringSearchField'" >> {
              AssigneeId.asSearchValue("") must beEqualTo(EmptyStringSearchField.asRight)
            }

            "Valid input, integer" >> {
              AssigneeId.asSearchValue("1") must beEqualTo(zendesk.model.value.AssigneeId(1).asRight)
            }

            "Invalid input, non-integer" >> {
              AssigneeId.asSearchValue("a") must beEqualTo(InvalidArgumentError("'a' is not Integer value").asLeft)
            }
          }
        }

        "when non-empty string value is acceptable" >> {
          "SubmitterId" >> {
            "empty string is not allowed" >> {
              SubmitterId.asSearchValue("") must beEqualTo(emptyStringError)
            }

            "Valid input, integer" >> {
              SubmitterId.asSearchValue("1") must beEqualTo(zendesk.model.value.SubmitterId(1).asRight)
            }

            "Invalid input, non-integer" >> {
              SubmitterId.asSearchValue("a") must beEqualTo(InvalidArgumentError("'a' is not Integer value").asLeft)
            }
          }
        }
      }

      "Should be Boolean value" >> {
        "when non-empty string value is acceptable" >> {
          "HasIncidents" >> {
            "empty string is allowed but return as 'EmptyStringSearchField'" >> {
              HasIncidents.asSearchValue("") must beEqualTo(EmptyStringSearchField.asRight)
            }

            "Valid input, boolean" >> {
              HasIncidents.asSearchValue("true") must beEqualTo(zendesk.model.value.HasIncidents(true).asRight)
              HasIncidents.asSearchValue("false") must beEqualTo(zendesk.model.value.HasIncidents(false).asRight)
            }

            "Invalid input, non-boolean" >> {
              HasIncidents.asSearchValue("a") must beEqualTo(InvalidArgumentError("'a' is not Boolean value").asLeft)
            }
          }
        }
      }

      "Should be UUID value" >> {
        "when non-empty string value is acceptable" >> {
          "TicketId" >> {
            "empty string is not allowed" >> {
              TicketId.asSearchValue("") must beEqualTo(emptyStringError)
            }

            "Valid input, uuid" >> {
              val uuid = UUID.randomUUID()
              TicketId.asSearchValue(uuid.toString) must beEqualTo(zendesk.model.value.TicketId(uuid).asRight)
            }

            "Invalid input, non-uuid" >> {
              TicketId.asSearchValue("a") must beEqualTo(InvalidArgumentError("'a' is not UUID value").asLeft)
            }
          }

          "ExternalId" >> {
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
        "when any string value is acceptable include empty string" >> {
          "DueAt" >> {
            "empty string is not allowed" >> {
              DueAt.asSearchValue("") must beEqualTo(EmptyStringSearchField.asRight)
            }

            "Valid input, datetime" >> {
              val dataTimeString = "2016-04-15T05:19:46 -10:00"
              val expectedDateTime = DateTime.parse("2016-04-15T05:19:46-10:00")
              DueAt.asSearchValue(dataTimeString) must beEqualTo(
                zendesk.model.value.ZenDateTime(expectedDateTime).asRight)
            }

            "Invalid input, non-datetime" >> {
              DueAt.asSearchValue("a") must beEqualTo(InvalidArgumentError("'a' is not DateTime value").asLeft)
            }
          }
        }

        "when non-empty string value is acceptable" >> {
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

      "Should be Enum value" >> {
        "when any string value is acceptable include empty string" >> {
          "Type" >> {
            "empty string is not allowed" >> {
              Type.asSearchValue("") must beEqualTo(EmptyStringSearchField.asRight)
            }

            "Valid input, enum" >> {
              Type.asSearchValue("incident") must beEqualTo(zendesk.model.value.Incident.asRight)
              Type.asSearchValue("problem") must beEqualTo(zendesk.model.value.Problem.asRight)
              Type.asSearchValue("question") must beEqualTo(zendesk.model.value.Question.asRight)
              Type.asSearchValue("task") must beEqualTo(zendesk.model.value.Task.asRight)
            }

            "Invalid input, non-enum" >> {
              val asLeft =
                InvalidArgumentError("'a' is not Type('incident', 'problem', 'question', 'task') value").asLeft

              Type.asSearchValue("a") must beEqualTo(asLeft)
            }
          }
        }

        "when non-empty string value is acceptable" >> {
          "Via" >> {
            "empty string is not allowed" >> {
              Via.asSearchValue("") must beEqualTo(emptyStringError)
            }

            "Valid input, enum" >> {
              Via.asSearchValue("web") must beEqualTo(zendesk.model.value.Web.asRight)
              Via.asSearchValue("chat") must beEqualTo(zendesk.model.value.Chat.asRight)
              Via.asSearchValue("voice") must beEqualTo(zendesk.model.value.Voice.asRight)
            }

            "Invalid input, non-enum" >> {
              val asLeft = InvalidArgumentError("'a' is not Via('web', 'chat', 'voice') value").asLeft
              Via.asSearchValue("a") must beEqualTo(asLeft)
            }
          }

          "Status" >> {
            "empty string is not allowed" >> {
              Status.asSearchValue("") must beEqualTo(emptyStringError)
            }

            "Valid input, enum" >> {
              Status.asSearchValue("closed") must beEqualTo(zendesk.model.value.Closed.asRight)
              Status.asSearchValue("hold") must beEqualTo(zendesk.model.value.Hold.asRight)
              Status.asSearchValue("open") must beEqualTo(zendesk.model.value.Open.asRight)
              Status.asSearchValue("pending") must beEqualTo(zendesk.model.value.Pending.asRight)
              Status.asSearchValue("solved") must beEqualTo(zendesk.model.value.Solved.asRight)
            }

            "Invalid input, non-enum" >> {
              val asLeft =
                InvalidArgumentError("'a' is not Status('closed', 'hold', 'open', 'pending', 'solved') value").asLeft
              Status.asSearchValue("a") must beEqualTo(asLeft)
            }
          }

          "Priority" >> {
            "empty string is not allowed" >> {
              Priority.asSearchValue("") must beEqualTo(emptyStringError)
            }

            "Valid input, enum" >> {
              Priority.asSearchValue("urgent") must beEqualTo(zendesk.model.value.Urgent.asRight)
              Priority.asSearchValue("high") must beEqualTo(zendesk.model.value.High.asRight)
              Priority.asSearchValue("normal") must beEqualTo(zendesk.model.value.Normal.asRight)
              Priority.asSearchValue("low") must beEqualTo(zendesk.model.value.Low.asRight)
            }

            "Invalid input, non-enum" >> {
              val asLeft = InvalidArgumentError("'a' is not Priority('urgent', 'high', 'normal', 'low') value").asLeft
              Priority.asSearchValue("a") must beEqualTo(asLeft)
            }
          }
        }
      }
    }
  }
}
