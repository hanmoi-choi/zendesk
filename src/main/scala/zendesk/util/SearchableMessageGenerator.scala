package zendesk.util

import zendesk.model.Searchable

object SearchableMessageGenerator {
  def forObject[T <: Searchable](obj: T): String = {
    val fields = obj.getClass.getDeclaredFields.map(_.getName).toList
    s"""
       |-------------------------
       |Search ${obj.getClass.getSimpleName}s with
       |-------------------------
       |${fields.mkString("\n")}
       |
       |""".stripMargin
  }
}
