package services

import domain.{AgileEngineServiceError, ApiData, Picture, PictureList, PictureItem, TokenRequest}
import javax.inject.Inject
import play.api.libs.ws.WSClient
import domain.ImplicitsJson._
import global.ApiResult
import play.api.Logging
import play.api.libs.ws.ahc.AhcWSResponse

import scala.concurrent.ExecutionContext

class APIPhotosService @Inject() (wsClient: WSClient, apiData: ApiData)(implicit ec: ExecutionContext) extends Logging {

  def getToken: ApiResult[String] = {
    wsClient
      .url(apiData.url)
      .post(Map("apiKey" -> apiData.apiKey))
      .map{ response =>
        response.status match {
          case 200 => response.json.validate[TokenRequest].fold(
            error => {
              logger.error(s"Error while connect AgileEngine Photo Rest api: $error")
              Left(AgileEngineServiceError(error.toString()))
            },
            tokenRequest => {
              Right(tokenRequest.token)
            }
          )
          case error => Left(AgileEngineServiceError(s"Unable to connect git AgileEngine Photo Rest api: $error"))
        }
    }
  }

  def getImage(id: String, token: String): ApiResult[Picture] = {
    wsClient
      .url(apiData.url + "/images/" + id)
      .withHttpHeaders(("Authorization", "Beaver " + token))
      .get()
      .map{ response =>
      response.status match {
        case 200 => response.json.validate[Picture].fold(
          error => {
            logger.error(s"Error while connect AgileEngine Photo Rest api: $error")
            Left(AgileEngineServiceError(error.toString()))
          },
          picture => Right(picture)
        )
        case error => Left(AgileEngineServiceError(s"Unable to connect git AgileEngine Photo Rest api: $error"))
      }
    }
  }

  def getImageList(token: String, page: Int): ApiResult[PictureList] = {
    wsClient
      .url(apiData.url + "/images")
      .withHttpHeaders(("Authorization", "Beaver " + token))
      .get()
      .map{ response =>
        response.status match {
          case 200 => response.json.validate[PictureList].fold(
            error => {
              logger.error(s"Error while connect AgileEngine Photo Rest api: $error")
              Left(AgileEngineServiceError(error.toString()))
            },
            picture => Right(picture)
          )
          case error => Left(AgileEngineServiceError(s"Unable to connect git AgileEngine Photo Rest api: $error"))
        }
      }
  }
}
