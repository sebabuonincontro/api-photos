package domain

case class ApiData(url: String, apiKey: String)

case class TokenRequest(auth: Boolean, token: String)

//Domain Classes
case class PictureList(
  pictures: List[PictureItem],
  page: Int,
  pageCount: Int,
  hasMore: Boolean)

case class PictureItem(
  id: String,
  cropped_picture: String)

case class Picture(
  id: String,
  author: String,
  camera: String,
  tags: String,
  cropped_picture: String,
  full_picture: String) {

  def toMap() = {
    Map(
      "id" -> id,
      "author" -> author,
      "camera" -> camera,
      "tags" -> tags,
      "cropped_picture" -> cropped_picture,
      "full_picture" -> full_picture
    )
  }
}

object Picture {
  def from(map: Map[String, String]) = {
    Picture(
      id = map.getOrElse("id", ""),
      author = map.getOrElse("author", ""),
      camera = map.getOrElse("camera", ""),
      tags = map.getOrElse("tags", ""),
      cropped_picture = map.getOrElse("cropped_picture", ""),
      full_picture = map.getOrElse("full_picture", "")
    )
  }
}




