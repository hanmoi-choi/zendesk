package zendesk.model

import zendesk.model.value.SearchValue

case class QueryParams(searchKey: Searchable.Keys, searchTerm: String, searchValue: SearchValue)
