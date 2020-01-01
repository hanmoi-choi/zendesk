package zendesk.dsl

import zendesk.model.AppError

trait Console[F[_]] {
  def out(value: String): F[Either[AppError, Unit]]

  def in(): F[Either[AppError, String]]
}

object Console {
  def apply[F[_]](implicit C: Console[F]): Console[F] = C

  def out[F[_]: Console](value: String): F[Either[AppError, Unit]] =
    Console[F].out(value)

  def in[F[_]: Console](): F[Either[AppError, String]] =
    Console[F].in()
}
