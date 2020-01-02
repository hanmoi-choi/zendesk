package zendesk.model.value

import cats.syntax.option._
import io.circe.{Decoder, Encoder}

/*
❯ cat tickets.json |jq 'map(.type) | unique'
[
  null,
  "incident",
  "problem",
  "question",
  "task"
]
 */
sealed case class Type(name: String) extends SearchValue {
  override def rawValue: String = name
}

object Incident extends Type("incident")

object Problem extends Type("problem")

object Question extends Type("question")

object Task extends Type("task")

object Type {
  val all = List(Incident, Problem, Question, Task)

  def fromString(value: String): Option[Type] = {
    value.toLowerCase() match {
      case "incident" => Incident.some
      case "problem" => Problem.some
      case "question" => Question.some
      case "task" => Task.some
      case _ => None
    }
  }

  implicit val encoderEvent: Encoder[Type] = {
    Encoder[String].contramap(_.name)
  }

  implicit val decoderEvent: Decoder[Type] = {
    Decoder[String].map { name =>
      all.find(_.name == name).getOrElse(Type(name))
    }
  }
}
