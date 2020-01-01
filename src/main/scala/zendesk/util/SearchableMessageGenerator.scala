package zendesk.util

import zendesk.model.Searchable

object SearchableMessageGenerator {
  def forObject[T <: Searchable](obj: T, title: String): String = {
    val fields = obj.getClass.getDeclaredFields.map(_.getName).toList
    s"""
       |-------------------------
       |$title
       |-------------------------
       |${fields.mkString("\n")}
       |
       |""".stripMargin
  }
}
