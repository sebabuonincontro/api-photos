package repositories

import akka.Done
import com.redis.RedisClient
import domain.{Picture, PictureList, PictureNotFound}
import global.ApiResult
import javax.inject.Inject
import play.api.Logging

import scala.concurrent.ExecutionContext

class PhotoRepository @Inject() (implicit redis: RedisClient, ec: ExecutionContext) extends Logging with RedisLike {

  val listKey = "picture:list"
  val pictureKey = "picture:"

  def savePicture(picture: Picture) = {
    doRedisFor{ redis =>
      redis.hmset(pictureKey + picture.id, picture.toMap())
      Right(Done)
    }
  }

  def savePictureList(pictureList: PictureList) = {
    doRedisFor{ redis =>
      pictureList.pictures.foreach{ picture =>
        redis.lpush(listKey, picture.id)
      }
      Right(Done)
    }
  }

  def getPicture(id: String): ApiResult[Picture] = {
    doRedisFor { redis =>
      redis.hmget[String, String](pictureKey + id) match {
        case Some(map) => Right(Picture from map)
        case None => Left(PictureNotFound(id))
      }
    }
  }

  def getPictureList(page: Int, size: Int): ApiResult[List[Option[String]]] = {
    val start = (page - 1)  * size
    val last = start + size
    doRedisFor{ redis =>
      Right(redis.lrange(listKey, start, last).getOrElse(List[Option[String]]()))
    }
  }
}
