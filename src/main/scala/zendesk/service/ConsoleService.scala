package zendesk.service

trait ConsoleService[F[_]] {
  def print(string: String): F[Unit]
  def read(): F[String]
}
