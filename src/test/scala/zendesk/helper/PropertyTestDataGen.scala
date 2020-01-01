package zendesk.helper

import com.fortysevendeg.scalacheck.datetime.joda.ArbitraryJoda.genDateTime
import org.scalacheck.{Arbitrary, Gen}
import zendesk.model.value._
import zendesk.model.{Organization, Ticket, User}

object PropertyTestDataGen {

  private implicit val idGen: Gen[Id] = Gen.posNum[Long].map(Id(_))
  private implicit val nameGen: Gen[Name] = Gen.alphaStr.map(Name(_))
  private implicit val domainNamesGen: Gen[List[DomainName]] = Gen.listOf(Gen.alphaStr.map(DomainName(_)))
  private implicit val detailsGen: Gen[Details] = Gen.alphaStr.map(Details(_))
  private implicit val sharedTicketsGen: Gen[SharedTickets] = Gen.oneOf(true, false).map(SharedTickets(_))
  private implicit val tagsGen: Gen[List[Tag]] = Gen.listOf(Gen.alphaStr.map(Tag(_)))
  private implicit val externalIdGen: Gen[ExternalId] = Gen.uuid.map(ExternalId(_))
  private implicit val ticketUrlGen: Gen[Url] = Gen.uuid.map { id =>
    s"http://initech.zendesk.com/api/v2/tickets/$id.json"
  }.map(Url(_))
  private implicit val userUrlGen: Gen[Url] = Gen
    .posNum[Long]
    .map { id =>
      s"http://initech.zendesk.com/api/v2/users/$id.json"
    }
    .map(Url(_))
  private implicit val orgUrlGen: Gen[Url] = Gen
    .posNum[Long]
    .map { id =>
      s"http://initech.zendesk.com/api/v2/organizations/$id.json"
    }
    .map(Url(_))
  private implicit val dateTimeGen: Gen[ZenDateTime] = genDateTime.map(ZenDateTime(_))
  private implicit val optionalDateTimeGen: Gen[Option[ZenDateTime]] = Gen.option(genDateTime.map(ZenDateTime(_)))

  private implicit val orgGen: Gen[Organization] = for {
    id            <- idGen
    url           <- orgUrlGen
    externalId    <- externalIdGen
    name          <- nameGen
    domainNames   <- domainNamesGen
    details       <- detailsGen
    sharedTickets <- sharedTicketsGen
    tags          <- tagsGen
    createdAt     <- dateTimeGen
  } yield Organization(
    id = id,
    url = url,
    externalId = externalId,
    name = name,
    domainNames = domainNames,
    createdAt = createdAt,
    details = details,
    sharedTickets = sharedTickets,
    tags = tags
  )

  private implicit val activeGen: Gen[Active] = Gen.oneOf(true, false).map(Active(_))
  private implicit val verifiedGen: Gen[Option[Verified]] = Gen.option(Gen.oneOf(true, false).map(Verified(_)))
  private implicit val sharedGen: Gen[Shared] = Gen.oneOf(true, false).map(Shared(_))
  private implicit val suspendedGen: Gen[Suspended] = Gen.oneOf(true, false).map(Suspended(_))
  private implicit val localeGen: Gen[Option[Locale]] = Gen.option(Gen.oneOf("en-AU", "en-UK", "ko-KO").map(Locale(_)))
  private implicit val timezoneGen: Gen[Option[Timezone]] =
    Gen.option(Gen.oneOf("Melbourne", "Sydney", "Filand").map(Timezone(_)))
  private implicit val signatureGen: Gen[Signature] = Gen.alphaStr.map(Signature(_))
  private implicit val aliasGen: Gen[Option[Alias]] = Gen.option(Gen.alphaStr.map(Alias(_)))
  private implicit val emailGen: Gen[Option[Email]] = Gen.option(Gen.alphaStr.map(Email(_)))
  private implicit val phoneGen: Gen[Phone] = Gen.alphaStr.map(Phone(_))
  private implicit val roleGen: Gen[Role] = Gen.oneOf(Admin, EndUser, Agent)
  private implicit val orgIdGen: Gen[Option[OrganizationId]] = Gen.option(Gen.posNum[Long].map(OrganizationId(_)))

  private implicit val userGen: Gen[User] = for {
    id             <- idGen
    url            <- userUrlGen
    externalId     <- externalIdGen
    name           <- nameGen
    alias          <- aliasGen
    createdAt      <- dateTimeGen
    active         <- activeGen
    verified       <- verifiedGen
    shared         <- sharedGen
    tags           <- tagsGen
    locale         <- localeGen
    timezone       <- timezoneGen
    lastLoginAt    <- dateTimeGen
    email          <- emailGen
    phone          <- phoneGen
    signature      <- signatureGen
    organizationId <- orgIdGen
    suspended      <- suspendedGen
    role           <- roleGen
  } yield User(
    id = id,
    url = url,
    externalId = externalId,
    name = name,
    alias = alias,
    createdAt = createdAt,
    active = active,
    verified = verified,
    shared = shared,
    locale = locale,
    timezone = timezone,
    lastLoginAt = lastLoginAt,
    email = email,
    phone = phone,
    signature = signature,
    organizationId = organizationId,
    tags = tags,
    suspended = suspended,
    role = role
  )

  private implicit val typeGen: Gen[Option[Type]] = Gen.option(Gen.oneOf(Incident, Problem, Question, Task))
  private implicit val priorityGen: Gen[Priority] = Gen.oneOf(Urgent, High, Normal, Low)
  private implicit val statusGen: Gen[Status] = Gen.oneOf(Closed, Hold, Open, Pending, Solved)
  private implicit val viaGen: Gen[Via] = Gen.oneOf(Web, Voice, Chat)

  private implicit val ticketIdGen: Gen[TicketId] = Gen.uuid.map(TicketId(_))
  private implicit val subjectGen: Gen[Subject] = Gen.alphaStr.map(Subject(_))
  private implicit val descriptionGen: Gen[Option[Description]] = Gen.option(Gen.alphaStr.map(Description(_)))
  private implicit val submitterIdGen: Gen[SubmitterId] = Gen.posNum[Long].map(SubmitterId(_))
  private implicit val assigneeIdGen: Gen[Option[AssigneeId]] = Gen.option(Gen.posNum[Long].map(AssigneeId(_)))
  private implicit val hasIncidentsGen: Gen[HasIncidents] = Gen.oneOf(true, false).map(HasIncidents(_))

  private implicit val ticketGen: Gen[Ticket] = for {
    ticketId       <- ticketIdGen
    url            <- ticketUrlGen
    externalId     <- externalIdGen
    createdAt      <- dateTimeGen
    ticketType     <- typeGen
    subject        <- subjectGen
    description    <- descriptionGen
    priority       <- priorityGen
    status         <- statusGen
    submitterId    <- submitterIdGen
    assigneeId     <- assigneeIdGen
    organizationId <- orgIdGen
    tags           <- tagsGen
    hasIncidents   <- hasIncidentsGen
    dueAt          <- optionalDateTimeGen
    via            <- viaGen
  } yield Ticket(
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

  implicit val orgArbitrary: Arbitrary[Organization] = Arbitrary(orgGen)
  implicit val userArbitrary: Arbitrary[User] = Arbitrary(userGen)
  implicit val ticketArbitrary: Arbitrary[Ticket] = Arbitrary(ticketGen)

  implicit val usersArbitrary: Arbitrary[List[User]] = Arbitrary(Gen.listOfN(20000, userGen))
}
