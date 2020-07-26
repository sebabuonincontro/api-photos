package domain

import play.api.libs.json.Json

object ImplicitsJson {

  implicit val tokenRequestReads = Json.reads[TokenRequest]

  implicit val pictureReads = Json.reads[Picture]
  implicit val pictureWrites = Json.writes[Picture]

  implicit val pictureResponseReads = Json.reads[PictureItem]
  implicit val pictureResponseWrites = Json.writes[PictureItem]

  implicit val pictureListResponseReads = Json.reads[PictureList]
  implicit val pictureListResponseWrites = Json.writes[PictureList]

}
