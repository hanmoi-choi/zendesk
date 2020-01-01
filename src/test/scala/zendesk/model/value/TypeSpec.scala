package zendesk.model.value

import cats.syntax.option._
import org.specs2.mutable.Specification

class TypeSpec extends Specification {
  "Type" should {
    "from String; case insensitive" in {
      "when string 'incident'" >> {
        Type.fromString("incident") must beEqualTo(Incident.some)
        Type.fromString("INCIDENT") must beEqualTo(Incident.some)
      }

      "when string 'problem'" >> {
        Type.fromString("problem") must beEqualTo(Problem.some)
        Type.fromString("PROBLEM") must beEqualTo(Problem.some)
      }

      "when string 'question'" >> {
        Type.fromString("question") must beEqualTo(Question.some)
        Type.fromString("QUESTION") must beEqualTo(Question.some)
      }

      "when string 'task'" >> {
        Type.fromString("task") must beEqualTo(Task.some)
        Type.fromString("TASK") must beEqualTo(Task.some)
      }

      "when string 'foobar'" >> {
        Type.fromString("foobar") must beNone
      }
    }
  }
}

