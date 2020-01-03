package zendesk.model

import io.circe.syntax._
import io.circe.{Decoder, Encoder, HCursor, Json}
import zendesk.model.value._

case class Ticket(
  id: TicketId,
  url: Url,
  externalId: ExternalId,
  createdAt: ZenDateTime,
  `type`: Option[Type],
  subject: Subject,
  description: Option[Description],
  priority: Priority,
  status: Status,
  submitterId: SubmitterId,
  assigneeId: Option[AssigneeId],
  organizationId: Option[OrganizationId],
  tags: List[Tag],
  hasIncidents: HasIncidents,
  dueAt: Option[ZenDateTime],
  via: Via
) extends Searchable {
  def pairWithTag(): List[(Tag, Ticket)] = tags.map((_, this))

  override def asFullDataString: String = trimJsonString(this.asJson.spaces2)

  override def asSimpleDataString: String = trimJsonString(Ticket.simpleJson(this).spaces2)
}

object Ticket {
  def simpleJson(tk: Ticket): Json =
    Json.obj(
      ("ticket_subject", tk.subject.asJson),
      ("ticket_priority", tk.priority.asJson),
      ("ticket_status", tk.status.asJson)
    )

  implicit val encodeTicket: Encoder[Ticket] = (tk: Ticket) =>
    Json.obj(
      ("_id", tk.id.asJson),
      ("url", tk.url.asJson),
      ("external_id", tk.externalId.asJson),
      ("created_at", tk.createdAt.asJson),
      ("type", tk.`type`.asJson),
      ("subject", tk.subject.asJson),
      ("description", tk.description.asJson),
      ("priority", tk.priority.asJson),
      ("status", tk.status.asJson),
      ("submitter_id", tk.submitterId.asJson),
      ("assignee_id", tk.assigneeId.asJson),
      ("organization_id", tk.organizationId.asJson),
      ("tags", tk.tags.asJson),
      ("has_incidents", tk.hasIncidents.asJson),
      ("due_at", tk.dueAt.asJson),
      ("via", tk.via.asJson)
    )

  implicit val decodeTicket: Decoder[Ticket] = (c: HCursor) =>
    for {
      ticketId       <- c.downField("_id").as[TicketId]
      url            <- c.downField("url").as[Url]
      externalId     <- c.downField("external_id").as[ExternalId]
      createdAt      <- c.downField("created_at").as[ZenDateTime]
      ticketType     <- c.downField("type").as[Option[Type]]
      subject        <- c.downField("subject").as[Subject]
      description    <- c.downField("description").as[Option[Description]]
      priority       <- c.downField("priority").as[Priority]
      status         <- c.downField("status").as[Status]
      submitterId    <- c.downField("submitter_id").as[SubmitterId]
      assigneeId     <- c.downField("assignee_id").as[Option[AssigneeId]]
      organizationId <- c.downField("organization_id").as[Option[OrganizationId]]
      tags           <- c.downField("tags").as[List[Tag]]
      hasIncidents   <- c.downField("has_incidents").as[HasIncidents]
      dueAt          <- c.downField("due_at").as[Option[ZenDateTime]]
      via            <- c.downField("via").as[Via]
    } yield {
      new Ticket(
        id = ticketId,
        url = url,
        externalId = externalId,
        createdAt = createdAt,
        `type` = ticketType,
        subject = subject,
        description = description,
        priority = priority,
        status = status,
        submitterId = submitterId,
        assigneeId = assigneeId,
        organizationId = organizationId,
        tags = tags,
        hasIncidents = hasIncidents,
        dueAt = dueAt,
        via = via
      )
    }
}
