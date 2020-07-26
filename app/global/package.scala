import domain.ApiError
import global.ApiResult

import scala.concurrent.{ExecutionContext, Future}

package object global {

  type ApiEither[+T] = Either[ApiError, T]
  type ApiResult[+T] = Future[ApiEither[T]]

  object ApiResult {
    def apply[T](t: T): ApiResult[T] = Future.successful(Right(t))
    def error(error: ApiError) = Future.successful(Left(error))
  }

  implicit class ApiResultExtended[A](apiResult: ApiResult[A]) {

    def innerFlatMap[B](f: A => ApiResult[B])(implicit ec: ExecutionContext): ApiResult[B] =
      apiResult.flatMap(_.fold(ApiResult.error, f))

    def innerMap[B](f: A => B)(implicit ec: ExecutionContext): ApiResult[B] =
      apiResult.map(_.fold(Left(_), v => Right(f(v))))

    def innerHandleError(f: ApiError => A)(implicit ec: ExecutionContext): ApiResult[A] =
      apiResult map {
        case Left(err)    => Right(f(err))
        case Right(value) => Right(value)
      }
  }
}
