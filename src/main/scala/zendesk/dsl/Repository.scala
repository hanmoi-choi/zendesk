package zendesk.dsl

import zendesk.model.{AppError, QueryParams, Searchable}
import zendesk.util.SearchDatabase

trait Repository[F[_]] {
  def query(params: QueryParams)(implicit DB: SearchDatabase): F[Either[AppError, Vector[Searchable]]]
}

object Repository {
  def apply[F[_]](implicit R: Repository[F]): Repository[F] = R

  def query[F[_]: Repository](params: QueryParams)(
    implicit DB: SearchDatabase): F[Either[AppError, Vector[Searchable]]] =
    Repository[F].query(params)
}
