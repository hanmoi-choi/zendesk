package zendesk.model

import io.circe.syntax._
import io.circe.{Decoder, Encoder, HCursor, Json}
import zendesk.model.value._

case class Ticket(
                   id: TicketId,
                   url: Url,
                   externalId: ExternalId,
                   createdAt: DateTime,
                   `type`: Option[Type],
                   subject: Subject,
                   description: Description,
                   priority: Priority,
                   status: Status,
                   submitterId: SubmitterId,
                   assigneeId: AssigneeId,
                   organizationId: OrganizationId,
                   tags: List[Tag],
                   hasIncidents: HasIncidents,
                   dueAt: DateTime,
                   via: Via
                 )

object Ticket {
  implicit val encodeTicket: Encoder[Ticket] = (tk: Ticket) => Json.obj(
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
    ("via", tk.via.asJson),
  )

  implicit val decodeTicket: Decoder[Ticket] = (c: HCursor) => for {
    ticketId <- c.downField("_id").as[TicketId]
    url <- c.downField("url").as[Url]
    externalId <- c.downField("external_id").as[ExternalId]
    createdAt <- c.downField("created_at").as[DateTime]
    ticketType <- c.downField("type").as[Option[Type]]
    subject <- c.downField("subject").as[Subject]
    description <- c.downField("description").as[Description]
    priority <- c.downField("priority").as[Priority]
    status <- c.downField("status").as[Status]
    submitterId <- c.downField("submitter_id").as[SubmitterId]
    assigneeId <- c.downField("assignee_id").as[AssigneeId]
    organizationId <- c.downField("organization_id").as[OrganizationId]
    tags <- c.downField("tags").as[List[Tag]]
    hasIncidents <- c.downField("has_incidents").as[HasIncidents]
    dueAt <- c.downField("due_at").as[DateTime]
    via <- c.downField("via").as[Via]
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