package services

import cats.data.EitherT
import cats.implicits._
import domain.Picture
import javax.inject.Inject
import repositories.PhotoRepository
import global._

import scala.concurrent.ExecutionContext

class PhotosService @Inject() (photoRepository: PhotoRepository)(implicit ec: ExecutionContext) {

  def getPictureList(page: Int, size: Int): ApiResult[List[String]] = {
    (for {
      list <- EitherT(photoRepository.getPictureList(page, size))
    } yield list.map(item => item.get)).value
  }

  def getPicture(id: String): ApiResult[Picture] = {
    photoRepository.getPicture(id)
  }
}
