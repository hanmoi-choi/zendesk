package zendesk.util.parser

sealed trait SearchUsersTerm

object SearchUsersTerm {
  case object Id extends SearchUsersTerm
  case object Url extends SearchUsersTerm
  case object ExternalId extends SearchUsersTerm
  case object Name extends SearchUsersTerm
  case object Alias extends SearchUsersTerm
  case object CreatedAt extends SearchUsersTerm
  case object Active extends SearchUsersTerm
  case object Verified extends SearchUsersTerm
  case object Shared extends SearchUsersTerm
  case object Locale extends SearchUsersTerm
  case object Timezone extends SearchUsersTerm
  case object LastLoginAt extends SearchUsersTerm
  case object Email extends SearchUsersTerm
  case object Phone extends SearchUsersTerm
  case object Signature extends SearchUsersTerm
  case object OrganizationId extends SearchUsersTerm
  case object Tags extends SearchUsersTerm
  case object Suspended extends SearchUsersTerm
  case object Role extends SearchUsersTerm
  case object Quit extends SearchUsersTerm
}




