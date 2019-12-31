package zendesk.dsl

trait Console[F[_]] {
  def print(string: String): F[Unit]
  def read(): F[String]
}
