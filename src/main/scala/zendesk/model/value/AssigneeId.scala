package zendesk.model.value

import io.circe.{Decoder, Encoder}

case class AssigneeId(value: Long)

object AssigneeId {
  implicit val encodeAssigneeId: Encoder[AssigneeId] = Encoder.encodeLong.contramap[AssigneeId](_.value)
  implicit val decodeAssigneeId: Decoder[AssigneeId] = Decoder.decodeLong.map(AssigneeId(_))
}
