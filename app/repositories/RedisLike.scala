package repositories

import com.redis.RedisClient
import domain.RedisError
import global.{ApiEither, ApiResult}

import scala.concurrent.{ExecutionContext, Future}

/**
 * Trait to execute all commands from Redis.
 */
trait RedisLike {

  def doRedisFor[T](f: RedisClient => ApiEither[T])(implicit redis: RedisClient, ec: ExecutionContext) : ApiResult[T] = {
    Future(f(redis))
      .recover{
        case error : Throwable => Left(RedisError(error.getMessage))
      }
  }
}
