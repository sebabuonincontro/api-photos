package actors

import actors.TokenActor.{ObtainList, SaveNewPictures}
import akka.Done
import akka.actor.Actor
import cats.data.EitherT
import cats.implicits._
import domain.PictureItem
import global.ApiResult
import javax.inject.Inject
import repositories.PhotoRepository
import services.APIPhotosService

import scala.concurrent.{ExecutionContext, Future}


/**
 * Provides connection with ApiEnginePhotoService and pull photos into cache (redis)
 * @param photoRepository
 * @param apiPhotosService
 * @param ec
 */
class TokenActor @Inject() (photoRepository: PhotoRepository, apiPhotosService: APIPhotosService)(implicit ec: ExecutionContext) extends Actor {

  override def receive: Receive = {

    case ObtainList() =>
      val result = for {
        token <- EitherT( apiPhotosService.getToken )
        list <- EitherT( persistList(token, 1, 2)) //TODO Set lastPage in 2 to start the recursive calls.
      } yield (list)
      sender() ! result.value

    case SaveNewPictures(list) =>
      for {
        token <- EitherT( apiPhotosService.getToken )
        _ <- EitherT( getAndSavePictures(token, list) )
      } yield ()
  }

  /**
   * Obtain all pictures data and persist in redis each other.
   * @param token
   * @param nextPage
   * @param lastPage
   * @return
   */
  private def persistList(token: String, nextPage: Int, lastPage: Int): ApiResult[List[PictureItem]] = {
    if(nextPage == lastPage) Future.successful(Right(List()))
    else {
      val result = for {
        list <- EitherT( apiPhotosService.getImageList(token, nextPage + 1) )
        _ <- EitherT( photoRepository.savePictureList(list) )
        nextList <- EitherT( persistList(token, nextPage +1, list.pageCount) )
      } yield (list.pictures ++ nextList)
      result.value
    }
  }

  /***
   * Get details of each picture and persist it inside cache (redis)
   * @param token
   * @param list
   * @return
   */
  private def getAndSavePictures(token: String, list: List[PictureItem]): ApiResult[Done] = {
    list.foreach{ item =>
      for {
        picture <- EitherT( apiPhotosService.getImage(item.id, token) )
        _ <- EitherT( photoRepository.savePicture(picture))
      } yield ()
    }
    Future.successful(Right(Done))
  }
}

object TokenActor {
  sealed trait PictureMessage
  case class ObtainList() extends PictureMessage
  case class SaveNewPictures(list: List[PictureItem]) extends PictureMessage
}
