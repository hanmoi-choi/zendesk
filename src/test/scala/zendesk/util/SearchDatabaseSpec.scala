package zendesk.util

import org.specs2.mutable.Specification
import zendesk.helper.SampleDataGen
import zendesk.model.value.{QueryParams, SearchObject}

class SearchDatabaseSpec extends Specification{
  "Build Search DB from the given Data" >> {
    val db = SearchDatabase()
    db.createUsersDB(Vector(SampleDataGen.user))

    "Data is for Users" >> {
      val searchObject = SearchObject.User

      "Could search users with id" >> {
        db.query(QueryParams(searchObject, "id", SampleDataGen.user.id)) must beEqualTo(Vector(SampleDataGen.user))
      }
    }
  }

}
