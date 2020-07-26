package domain

sealed trait ApiError {
  val type_error: String
  val message: String
}
case class AgileEngineServiceError(msg: String) extends ApiError {
  override val type_error: String = "error"
  override val message: String = s"Error while connect AgileEngine Photo Rest api: $msg"
}

case class RedisError(msg: String) extends ApiError {
  override val type_error: String = "error"
  override val message: String = s"Error while connect Redis: $msg"
}

case class PictureNotFound(id: String) extends ApiError {
  override val type_error: String = "not_found"
  override val message: String = s"Picture not found for id: $id"
}

case class PictureListNotFound(page: Int, size: Int) extends ApiError {
  override val type_error: String = "not_found"
  override val message: String = s"Picture List not found for page: $page and size: $size"
}
