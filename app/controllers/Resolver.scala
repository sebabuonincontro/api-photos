package controllers

import domain.{ApiError, PictureListNotFound, PictureNotFound}
import play.api.Logging
import play.api.mvc.{BaseController, Result}

import scala.concurrent.ExecutionContext

/**
 * Trait contains a resolver and an handler error for Controller purpose.
 */
trait Resolver { _ : BaseController with Logging =>

  /**
   *
   * @param result result of the operation
   * @param fnc fnc to apply in case of success
   * @param ec ExecutionContext
   * @return [[Result]]
   */
  def resolve[T]( result: Either[ApiError, T])(fnc: T => Result)(implicit ec: ExecutionContext): Result = {
    result match {
      case Right(v) => fnc(v)
      case Left(e: PictureNotFound) => NotFound(e.message)
      case Left(e: PictureListNotFound) => NotFound(e.message)
      case Left(e: ApiError) =>
        logger.error(s"Unexpected Error: ${e.message}")
        InternalServerError(e.message)
    }
  }
}
