package zendesk.helper

import com.fortysevendeg.scalacheck.datetime.joda.ArbitraryJoda.genDateTime
import org.scalacheck.{Arbitrary, Gen}
import zendesk.model.value._
import zendesk.model.{Organization, User}

object TestDataGen {

  private implicit val idGen: Gen[Id] = Gen.posNum[Long].map(Id(_))
  private implicit val nameGen: Gen[Name] = Gen.alphaStr.map(Name(_))
  private implicit val domainNamesGen: Gen[List[DomainName]] = Gen.listOf(Gen.alphaStr.map(DomainName(_)))
  private implicit val detailsGen: Gen[Details] = Gen.alphaStr.map(Details(_))
  private implicit val sharedTicketsGen = Gen.oneOf(true, false).map(SharedTickets(_))
  private implicit val tagsGen: Gen[List[Tag]] = Gen.listOf(Gen.alphaStr.map(Tag(_)))
  private implicit val externalIdGen = Gen.uuid.map(ExternalId(_))
  private implicit val ticketUrlGen = Gen.uuid.map { id => s"http://initech.zendesk.com/api/v2/tickets/$id.json" }.map(Url(_))
  private implicit val userUrlGen = Gen.posNum[Long].map { id => s"http://initech.zendesk.com/api/v2/users/$id.json" }.map(Url(_))
  private implicit val orgUrlGen = Gen.posNum[Long].map { id => s"http://initech.zendesk.com/api/v2/organizations/$id.json" }.map(Url(_))
  private implicit val dateTimeGen = genDateTime.map(ZenDateTime(_))

  private implicit val orgGen = for {
    id <- idGen
    url <- orgUrlGen
    externalId <- externalIdGen
    name <- nameGen
    domainNames <- domainNamesGen
    details <- detailsGen
    sharedTickets <- sharedTicketsGen
    tags <- tagsGen
    createdAt <- dateTimeGen
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
  private implicit val verifiedGen: Gen[Verified] = Gen.oneOf(true, false).map(Verified(_))
  private implicit val sharedGen: Gen[Shared] = Gen.oneOf(true, false).map(Shared(_))
  private implicit val suspendedGen: Gen[Suspended] = Gen.oneOf(true, false).map(Suspended(_))
  private implicit val localeGen: Gen[Locale] = Gen.oneOf("en-AU", "en-UK", "ko-KO").map(Locale(_))
  private implicit val timezoneGen: Gen[Timezone] = Gen.oneOf("Melbourne", "Sydney", "Filand").map(Timezone(_))
  private implicit val signatureGen: Gen[Signature] = Gen.alphaStr.map(Signature(_))
  private implicit val aliasGen: Gen[Alias] = Gen.alphaStr.map(Alias(_))
  private implicit val emailGen: Gen[Email] = Gen.alphaStr.map(Email(_))
  private implicit val phoneGen: Gen[Phone] = Gen.alphaStr.map(Phone(_))
  private implicit val roleGen: Gen[Role] = Gen.oneOf(Admin, EndUser, Agent)
  private implicit val orgIdGen: Gen[OrganizationId] = Gen.posNum[Long].map(OrganizationId(_))

  private implicit val userGen = for {
    id <- idGen
    url <- userUrlGen
    externalId <- externalIdGen
    name <- nameGen
    alias <- aliasGen
    createdAt <- dateTimeGen
    active <- activeGen
    verified <- verifiedGen
    shared <- sharedGen
    tags <- tagsGen
    locale <- localeGen
    timezone <- timezoneGen
    lastLoginAt <- dateTimeGen
    email <- emailGen
    phone <- phoneGen
    signature <- signatureGen
    organizationId <- orgIdGen
    suspended <- suspendedGen
    role <- roleGen
  } yield User(
    id = id,
    url = url,
    externalId = externalId,
    name = name,
    alias: Alias,
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

  implicit val orgArbitrary: Arbitrary[Organization] = Arbitrary(orgGen)
  implicit val userArbitrary: Arbitrary[User] = Arbitrary(userGen)
}

