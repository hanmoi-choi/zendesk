package zendesk.model.value

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
sealed case class Type(name: String) extends SearchValue

object Incident extends Type("incident")

object Problem extends Type("problem")

object Question extends Type("question")

object Task extends Type("task")

object Type {
  val all = List(Incident, Problem, Question, Task)

  implicit val encoderEvent: Encoder[Type] = {
    Encoder[String].contramap(_.name)
  }

  implicit val decoderEvent: Decoder[Type] = {
    Decoder[String].map { name =>
      all.find(_.name == name).getOrElse(Type(name))
    }
  }
}

