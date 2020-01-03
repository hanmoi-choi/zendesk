package zendesk.service

import java.util.UUID

import org.specs2.mutable.Specification
import zendesk.helper.TestDataFactory
import zendesk.model.value.{Id, TicketId}
import zendesk.model.{QueryParams, SearchResult, Searchable}
class SearchResultFormatterTest extends Specification {
  private val user = TestDataFactory.user
  private val organization = TestDataFactory.organization
  private val ticket = TestDataFactory.ticket
  private val formatter = SearchResultFormatter()

  "SearchResultFormatter" should {
    "Should return human readable format" in {
      "Primary Object is Users" >> {
        val searchResultTitle =
          s"""
             |---------------------------------------------------
             | Search Results for this parameters
             |   - Object: Users
             |   - Term: Id
             |   - Value: 1
             |---------------------------------------------------
             |""".stripMargin

        val expectedSearchResultForPrimaryObject =
          s"""
             |  _id : 1
             |  url : http://initech.zendesk.com/api/v2/users/1.json
             |  external_id : 74341f74-9c79-49d5-9611-87ef9b6eb75f
             |  name : Francisca Rasmussen
             |  alias : Miss Coffey
             |  created_at : 2016-05-21T11:10:28 -10:00
             |  active : true
             |  verified : true
             |  shared : false
             |  locale : en-AU
             |  timezone : Sri Lanka
             |  last_login_at : 2016-05-21T11:10:28 -10:00
             |  email : coffeyrasmussen@flotonic.com
             |  phone : 8335-422-718
             |  signature : Don't Worry Be Happy!
             |  organization_id : 119
             |  tags : [
             |    Springville
             |    Sutton
             |    Diaperville
             |  ]
             |  suspended : true
             |  role : admin
             |---------------------------------------------------""".stripMargin

        val queryParams = {
          QueryParams(Searchable.Users, "Id", Id(1))
        }
        val searchResult = SearchResult(queryParams, user, Map.empty)
        val expectedResultString = searchResultTitle + expectedSearchResultForPrimaryObject

        val result = formatter.format(Vector(searchResult))

        result must beEqualTo(expectedResultString)
      }
      "Primary Object is Tickets" >> {
        val searchResultTitle =
          s"""
             |---------------------------------------------------
             | Search Results for this parameters
             |   - Object: Tickets
             |   - Term: Id
             |   - Value: 436bf9b0-1147-4c0a-8439-6f79833bff5b
             |---------------------------------------------------
             |""".stripMargin

        val expectedSearchResultForPrimaryObject =
          s"""
             |  _id : 436bf9b0-1147-4c0a-8439-6f79833bff5b
             |  url : http://initech.zendesk.com/api/v2/tickets/436bf9b0-1147-4c0a-8439-6f79833bff5b.json
             |  external_id : 9210cdc9-4bee-485f-a078-35396cd74063
             |  created_at : 2016-04-28T11:19:34 -10:00
             |  type : incident
             |  subject : A Catastrophe in Korea (North)
             |  description : Nostrud ad sit velit cupidatat laboris ipsum nisi amet laboris
             |  priority : high
             |  status : pending
             |  submitter_id : 38
             |  assignee_id : 24
             |  organization_id : 116
             |  tags : [
             |    Ohio
             |    Pennsylvania
             |  ]
             |  has_incidents : false
             |  due_at : 2016-07-31T02:37:50 -10:00
             |  via : web
             |---------------------------------------------------""".stripMargin

        val queryParams = {
          QueryParams(Searchable.Tickets, "Id", TicketId(UUID.fromString("436bf9b0-1147-4c0a-8439-6f79833bff5b")))
        }
        val searchResult = SearchResult(queryParams, ticket, Map.empty)
        val expectedResultString = searchResultTitle + expectedSearchResultForPrimaryObject

        val result = formatter.format(Vector(searchResult))

        result must beEqualTo(expectedResultString)
      }
      "Primary Object is Organizations" >> {
        val searchResultTitle =
          s"""
             |---------------------------------------------------
             | Search Results for this parameters
             |   - Object: Organizations
             |   - Term: Id
             |   - Value: 101
             |---------------------------------------------------
             |""".stripMargin

        val expectedSearchResultForPrimaryObject =
          s"""
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
             |---------------------------------------------------""".stripMargin

        val queryParams = {
          QueryParams(Searchable.Organizations, "Id", Id(101))
        }
        val searchResult = SearchResult(queryParams, organization, Map.empty)
        val expectedResultString = searchResultTitle + expectedSearchResultForPrimaryObject

        val result = formatter.format(Vector(searchResult))

        result must beEqualTo(expectedResultString)
      }
    }
  }
}
