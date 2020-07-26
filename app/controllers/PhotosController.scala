package controllers

import domain.Picture
import javax.inject.Inject
import play.api.Logging
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import services.PhotosService
import domain.ImplicitsJson._

import scala.concurrent.ExecutionContext

/**
 * Api rest to obtain images information
 * @param cc
 * @param photosService
 * @param ec
 */
class PhotosController @Inject()(cc: ControllerComponents, photosService: PhotosService)(implicit ec: ExecutionContext)
  extends AbstractController(cc)
  with Resolver
  with Logging {

  def getImages(page: Int, size: Int) = Action.async { _ =>
    photosService.getPictureList(page, size).map (resolve(_) { result =>
      logger.info(s"Get Images SuccessFul.")
      Created(Json.toJson(result))
    })
  }

  def getImage(id: String) = Action.async {
    photosService.getPicture(id).map (resolve(_) { result: Picture =>
      logger.info(s"Get Image SuccessFul.")
      Created(Json.toJson(result))
    })
  }
}
