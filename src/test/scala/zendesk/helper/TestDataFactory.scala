package zendesk.helper

import java.util.UUID

import org.joda.time.DateTime
import zendesk.model.value._
import zendesk.model.{Organization, Ticket, User}

object TestDataFactory {
  val listOfSearchableFieldsForUsers =
    s"""
       |-------------------------
       |Search Users with
       |-------------------------
       |id
       |url
       |externalId
       |name
       |alias
       |createdAt
       |active
       |verified
       |shared
       |locale
       |timezone
       |lastLoginAt
       |email
       |phone
       |signature
       |organizationId
       |tags
       |suspended
       |role
       |-------------------------
       |""".stripMargin

  val listOfSearchableFieldsForTickets =
    s"""
       |-------------------------
       |Search Tickets with
       |-------------------------
       |id
       |url
       |externalId
       |createdAt
       |type
       |subject
       |description
       |priority
       |status
       |submitterId
       |assigneeId
       |organizationId
       |tags
       |hasIncidents
       |dueAt
       |via
       |-------------------------
       |""".stripMargin

  val listOfSearchableFieldsForOrganizations =
    s"""
       |-------------------------
       |Search Organizations with
       |-------------------------
       |id
       |url
       |externalId
       |name
       |domainNames
       |createdAt
       |details
       |sharedTickets
       |tags
       |-------------------------
       |""".stripMargin

  val rawUserJson =
    """{
      |  "_id" : 1,
      |  "url" : "http://initech.zendesk.com/api/v2/users/1.json",
      |  "external_id" : "74341f74-9c79-49d5-9611-87ef9b6eb75f",
      |  "name" : "Francisca Rasmussen",
      |  "alias" : "Miss Coffey",
      |  "created_at" : "2016-05-21T11:10:28 -10:00",
      |  "active" : true,
      |  "verified" : true,
      |  "shared" : false,
      |  "locale" : "en-AU",
      |  "timezone" : "Sri Lanka",
      |  "last_login_at" : "2016-05-21T11:10:28 -10:00",
      |  "email" : "coffeyrasmussen@flotonic.com",
      |  "phone" : "8335-422-718",
      |  "signature" : "Don't Worry Be Happy!",
      |  "organization_id" : 119,
      |  "tags" : [
      |    "Springville",
      |    "Sutton",
      |    "Diaperville"
      |  ],
      |  "suspended" : true,
      |  "role" : "admin"
      |}""".stripMargin

  private val userTags = List(Tag("Springville"), Tag("Sutton"), Tag("Diaperville"))
  val user: User = User(
    id = Id(1),
    url = Url("http://initech.zendesk.com/api/v2/users/1.json"),
    externalId = ExternalId(UUID.fromString("74341f74-9c79-49d5-9611-87ef9b6eb75f")),
    name = Name("Francisca Rasmussen"),
    alias = Some(Alias("Miss Coffey")),
    createdAt = ZenDateTime(DateTime.parse("2016-05-21T11:10:28-10:00")),
    active = Active(true),
    verified = Some(Verified(true)),
    shared = Shared(false),
    locale = Some(Locale("en-AU")),
    timezone = Some(Timezone("Sri Lanka")),
    lastLoginAt = ZenDateTime(DateTime.parse("2016-05-21T11:10:28-10:00")),
    email = Some(Email("coffeyrasmussen@flotonic.com")),
    phone = Phone("8335-422-718"),
    signature = Signature("Don't Worry Be Happy!"),
    organizationId = Some(OrganizationId(119)),
    tags = userTags,
    suspended = Suspended(true),
    role = Admin
  )

  val rawTicketJson: String =
    """{
      |  "_id" : "436bf9b0-1147-4c0a-8439-6f79833bff5b",
      |  "url" : "http://initech.zendesk.com/api/v2/tickets/436bf9b0-1147-4c0a-8439-6f79833bff5b.json",
      |  "external_id" : "9210cdc9-4bee-485f-a078-35396cd74063",
      |  "created_at" : "2016-04-28T11:19:34 -10:00",
      |  "type" : "incident",
      |  "subject" : "A Catastrophe in Korea (North)",
      |  "description" : "Nostrud ad sit velit cupidatat laboris ipsum nisi amet laboris",
      |  "priority" : "high",
      |  "status" : "pending",
      |  "submitter_id" : 38,
      |  "assignee_id" : 24,
      |  "organization_id" : 116,
      |  "tags" : [
      |    "Ohio",
      |    "Pennsylvania"
      |  ],
      |  "has_incidents" : false,
      |  "due_at" : "2016-07-31T02:37:50 -10:00",
      |  "via" : "web"
      |}""".stripMargin
  private val ticketTags = List(Tag("Ohio"), Tag("Pennsylvania"))
  val ticket: Ticket = Ticket(
    id = TicketId(UUID.fromString("436bf9b0-1147-4c0a-8439-6f79833bff5b")),
    url = Url("http://initech.zendesk.com/api/v2/tickets/436bf9b0-1147-4c0a-8439-6f79833bff5b.json"),
    externalId = ExternalId(UUID.fromString("9210cdc9-4bee-485f-a078-35396cd74063")),
    createdAt = ZenDateTime(DateTime.parse("2016-04-28T11:19:34-10:00")),
    `type` = Some(Incident),
    subject = Subject("A Catastrophe in Korea (North)"),
    description = Some(Description("Nostrud ad sit velit cupidatat laboris ipsum nisi amet laboris")),
    priority = High,
    status = Pending,
    submitterId = SubmitterId(38),
    assigneeId = Some(AssigneeId(24)),
    organizationId = Some(OrganizationId(116)),
    tags = ticketTags,
    hasIncidents = HasIncidents(false),
    dueAt = Some(ZenDateTime(DateTime.parse("2016-07-31T02:37:50-10:00"))),
    via = Web
  )

  val rawOrganizationJson: String =
    """{
      |  "_id" : 101,
      |  "url" : "http://initech.zendesk.com/api/v2/organizations/101.json",
      |  "external_id" : "9270ed79-35eb-4a38-a46f-35725197ea8d",
      |  "name" : "Enthaze",
      |  "domain_names" : [
      |    "kage.com",
      |    "ecratic.com",
      |    "endipin.com",
      |    "zentix.com"
      |  ],
      |  "created_at" : "2016-05-21T11:10:28 -10:00",
      |  "details" : "MegaCorp",
      |  "shared_tickets" : false,
      |  "tags" : [
      |    "Fulton",
      |    "West",
      |    "Rodriguez",
      |    "Farley"
      |  ]
      |}""".stripMargin
  private val domainName =
    List(DomainName("kage.com"), DomainName("ecratic.com"), DomainName("endipin.com"), DomainName("zentix.com"))
  private val orgTags = List(Tag("Fulton"), Tag("West"), Tag("Rodriguez"), Tag("Farley"))
  val organization: Organization = Organization(
    id = Id(101),
    url = Url("http://initech.zendesk.com/api/v2/organizations/101.json"),
    externalId = ExternalId(UUID.fromString("9270ed79-35eb-4a38-a46f-35725197ea8d")),
    name = Name("Enthaze"),
    domainNames = domainName,
    createdAt = ZenDateTime(DateTime.parse("2016-05-21T11:10:28-10:00")),
    details = Details("MegaCorp"),
    sharedTickets = SharedTickets(false),
    tags = orgTags
  )

  val expectedUserSearchResult =
    s"""
       |---------------------------------------------------
       | Search Results for this parameters
       |   - Object: Users
       |   - Term: Id
       |   - Value: 1
       |---------------------------------------------------
       |## Primary Object
       |
       |  _id : 1
       |  url : http://initech.zendesk.com/api/v2/users/1.json
       |  external_id : 74341f74-9c79-49d5-9611-87ef9b6eb75f
       |  name : Francisca Rasmussen
       |  alias : Miss Coffey
       |  created_at : 2016-04-15T05:19:46 -10:00
       |  active : true
       |  verified : true
       |  shared : false
       |  locale : en-AU
       |  timezone : Sri Lanka
       |  last_login_at : 2013-08-04T01:03:27 -10:00
       |  email : coffeyrasmussen@flotonic.com
       |  phone : 8335-422-718
       |  signature : Don't Worry Be Happy!
       |  organization_id : 119
       |  tags : [
       |    Springville
       |    Sutton
       |    Hartsville/Hartley
       |    Diaperville
       |  ]
       |  suspended : true
       |  role : admin
       |
       |## Relations
       |### Tickets as Submitter
       |
       |  ticket_subject : A Nuisance in Kiribati
       |  ticket_priority : high
       |  ticket_status : open
       |
       |  ticket_subject : A Nuisance in Saint Lucia
       |  ticket_priority : urgent
       |  ticket_status : pending
       |
       |### Tickets as Assignee
       |
       |  ticket_subject : A Problem in Russian Federation
       |  ticket_priority : low
       |  ticket_status : solved
       |
       |  ticket_subject : A Problem in Malawi
       |  ticket_priority : urgent
       |  ticket_status : solved
       |
       |### Organizations
       |
       |  organization_name : Multron
       |
       |---------------------------------------------------""".stripMargin

  val expectedTicketSearchResult =
    s"""
       |---------------------------------------------------
       | Search Results for this parameters
       |   - Object: Tickets
       |   - Term: Id
       |   - Value: 436bf9b0-1147-4c0a-8439-6f79833bff5b
       |---------------------------------------------------
       |## Primary Object
       |
       |  _id : 436bf9b0-1147-4c0a-8439-6f79833bff5b
       |  url : http://initech.zendesk.com/api/v2/tickets/436bf9b0-1147-4c0a-8439-6f79833bff5b.json
       |  external_id : 9210cdc9-4bee-485f-a078-35396cd74063
       |  created_at : 2016-04-28T11:19:34 -10:00
       |  type : incident
       |  subject : A Catastrophe in Korea (North)
       |  description : Nostrud ad sit velit cupidatat laboris ipsum nisi amet laboris ex exercitation amet et proident. Ipsum fugiat aute dolore tempor nostrud velit ipsum.
       |  priority : high
       |  status : pending
       |  submitter_id : 38
       |  assignee_id : 24
       |  organization_id : 116
       |  tags : [
       |    Ohio
       |    Pennsylvania
       |    American Samoa
       |    Northern Mariana Islands
       |  ]
       |  has_incidents : false
       |  due_at : 2016-07-31T02:37:50 -10:00
       |  via : web
       |
       |## Relations
       |### Users as Submitter
       |
       |  user_name : Elma Castro
       |  alias : Mr Georgette
       |  user_role : agent
       |
       |### Users as Assignee
       |
       |  user_name : Harris Côpeland
       |  alias : Miss Gates
       |  user_role : agent
       |
       |### Organizations
       |
       |  organization_name : Zentry
       |
       |---------------------------------------------------""".stripMargin
  val expectedOrgSearchResult =
    s"""
       |---------------------------------------------------
       | Search Results for this parameters
       |   - Object: Organizations
       |   - Term: Id
       |   - Value: 101
       |---------------------------------------------------
       |## Primary Object
       |
       |  _id : 101
       |  url : http://initech.zendesk.com/api/v2/organizations/101.json
       |  external_id : 9270ed79-35eb-4a38-a46f-35725197ea8d
       |  name : Enthaze
       |  domain_names : [
       |    kage.com
       |    ecratic.com
       |    endipin.com
       |    zentix.com
       |  ]
       |  created_at : 2016-05-21T11:10:28 -10:00
       |  details : MegaCorp
       |  shared_tickets : false
       |  tags : [
       |    Fulton
       |    West
       |    Rodriguez
       |    Farley
       |  ]
       |
       |## Relations
       |### Organizations
       |
       |  user_name : Loraine Pittman
       |  alias : Mr Ola
       |  user_role : admin
       |
       |  user_name : Francis Bailey
       |  alias : Miss Singleton
       |  user_role : agent
       |
       |  user_name : Haley Farmer
       |  alias : Miss Lizzie
       |  user_role : agent
       |
       |  user_name : Herrera Norman
       |  alias : Mr Vance
       |  user_role : end-user
       |
       |### Organizations
       |
       |  ticket_subject : A Drama in Portugal
       |  ticket_priority : low
       |  ticket_status : hold
       |
       |  ticket_subject : A Problem in Ethiopia
       |  ticket_priority : low
       |  ticket_status : hold
       |
       |  ticket_subject : A Problem in Turks and Caicos Islands
       |  ticket_priority : low
       |  ticket_status : pending
       |
       |  ticket_subject : A Problem in Guyana
       |  ticket_priority : normal
       |  ticket_status : closed
       |
       |---------------------------------------------------""".stripMargin
}
