package zendesk.service.parser

import cats.syntax.either._
import org.specs2.ScalaCheck
import org.specs2.mutable.Specification
import zendesk.model.ParseFailure
import zendesk.service.parser.TermsToSearchTickets._
import zendesk.service.parser.TermsToSearchTicketsParser.doParse

import scala.language.postfixOps


class TermsToSearchTicketsParserSpec extends Specification with ScalaCheck {
  "should parse 'id' as 'Id' term" >> {
    "as lowercase" >> {
      doParse("id") must beEqualTo(TicketId.asRight)
    }
    "as uppercase" >> {
      doParse("ID") must beEqualTo(TicketId.asRight)
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
      doParse("externalid") must beEqualTo(ExternalId.asRight)
    }
    "as uppercase" >> {
      doParse("EXTERNALID") must beEqualTo(ExternalId.asRight)
    }
  }

  "should parse 'type' as 'Type' term" >> {
    "as lowercase" >> {
      doParse("type") must beEqualTo(Type.asRight)
    }
    "as uppercase" >> {
      doParse("TYPE") must beEqualTo(Type.asRight)
    }
  }

  "should parse 'subject' as 'Type' term" >> {
    "as lowercase" >> {
      doParse("subject") must beEqualTo(Subject.asRight)
    }
    "as uppercase" >> {
      doParse("SUBJECT") must beEqualTo(Subject.asRight)
    }
  }

  "should parse 'description' as 'Type' term" >> {
    "as lowercase" >> {
      doParse("description") must beEqualTo(Description.asRight)
    }
    "as uppercase" >> {
      doParse("DESCRIPTION") must beEqualTo(Description.asRight)
    }
  }


  "should parse 'priority' as 'Priority' term" >> {
    "as lowercase" >> {
      doParse("priority") must beEqualTo(Priority.asRight)
    }
    "as uppercase" >> {
      doParse("PRIORITY") must beEqualTo(Priority.asRight)
    }
  }


  "should parse 'status' as 'Status' term" >> {
    "as lowercase" >> {
      doParse("status") must beEqualTo(Status.asRight)
    }
    "as uppercase" >> {
      doParse("STATUS") must beEqualTo(Status.asRight)
    }
  }

  "should parse 'submitterId' as 'SubmitterId' term" >> {
    "as lowercase" >> {
      doParse("submitterid") must beEqualTo(SubmitterId.asRight)
    }
    "as uppercase" >> {
      doParse("SUBMITTERID") must beEqualTo(SubmitterId.asRight)
    }

  }

  "should parse 'assigneeId' as 'AssigneeId' term" >> {
    "as lowercase" >> {
      doParse("assigneeid") must beEqualTo(AssigneeId.asRight)
    }
    "as uppercase" >> {
      doParse("ASSIGNEEID") must beEqualTo(AssigneeId.asRight)
    }
  }

  "should parse 'hasIncidents' as 'HasIncidents' term" >> {
    "as lowercase" >> {
      doParse("hasincidents") must beEqualTo(HasIncidents.asRight)
    }
    "as uppercase" >> {
      doParse("HASINCIDENTS") must beEqualTo(HasIncidents.asRight)
    }
  }

  "should parse 'dueAt' as 'DueAt' term" >> {
    "as lowercase" >> {
      doParse("dueat") must beEqualTo(DueAt.asRight)
    }
    "as uppercase" >> {
      doParse("DUEAT") must beEqualTo(DueAt.asRight)
    }
  }

  "should parse 'organizationId' as 'OrganizationId' term" >> {
    "as lowercase" >> {
      doParse("organizationid") must beEqualTo(OrganizationId.asRight)
    }
    "as uppercase" >> {
      doParse("ORGANIZATIONID") must beEqualTo(OrganizationId.asRight)
    }
  }

  "should parse 'vai' as 'Via' term" >> {
    "as lowercase" >> {
      doParse("via") must beEqualTo(Via.asRight)
    }
    "as uppercase" >> {
      doParse("VIA") must beEqualTo(Via.asRight)
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

  "Should parse 'quit' as 'Quit' command" >> {
    "as lowercase" >> {
      doParse("quit") must beEqualTo(Quit.asRight)
    }

    "as uppercase" >> {
      doParse("QUIT") must beEqualTo(Quit.asRight)
    }
  }

  "any other string inputs" >> prop {
    invalidCommand: String =>
      (invalidCommand != "id" &&
        invalidCommand.toLowerCase != "url" &&
        invalidCommand.toLowerCase != "externalId" &&
        invalidCommand.toLowerCase != "createdAt" &&
        invalidCommand.toLowerCase != "type" &&
        invalidCommand.toLowerCase != "subject" &&
        invalidCommand.toLowerCase != "description" &&
        invalidCommand.toLowerCase != "priority" &&
        invalidCommand.toLowerCase != "status" &&
        invalidCommand.toLowerCase != "submitterId" &&
        invalidCommand.toLowerCase != "assigneeId" &&
        invalidCommand.toLowerCase != "organizationId" &&
        invalidCommand.toLowerCase != "tags" &&
        invalidCommand.toLowerCase != "hasIncidents" &&
        invalidCommand.toLowerCase != "dueAt" &&
        invalidCommand.toLowerCase != "via" &&
        invalidCommand.toLowerCase != "quit"
        ) ==> prop {
        _: String =>

          val result = doParse(invalidCommand)
          val expectedError = ParseFailure(s"Cannot parse $invalidCommand as SearchTicketsTerm").asLeft

          result must beEqualTo(expectedError)
      }
  }.set(minTestsOk = 50, workers = 3)

}
