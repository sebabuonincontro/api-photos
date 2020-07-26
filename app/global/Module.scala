package global

import actors.TokenActor
import com.google.inject.{AbstractModule, Provides}
import com.redis.RedisClient
import domain.ApiData
import javax.inject.Singleton
import play.api.libs.concurrent.AkkaGuiceSupport
import play.api.{Configuration, Environment}

/**
 * Main Guice module
 * Add Configuration & Environment to Module Parameters when necessary.
 *
 * @param environment   Play Environment
 * @param configuration Play Configuration instance
 */
class Module (environment: Environment, configuration: Configuration) extends AbstractModule with AkkaGuiceSupport {


  override def configure = {
    //Here initialize the token actor to obtain data from Agile Engine Api
    bindActor[TokenActor]("token-actor")
  }

  /**
   * Provides Data about Api.
   * @return
   */
  @Provides
  def getApiData()= {
    ApiData(
      url = configuration.get[String]("agile-engine.token.url"),
      apiKey = configuration.get[String]("agile-engine.token.api-key"))
  }

  /**
   * Provides Redis Instance Configuration.
   * @return
   */
  @Provides @Singleton
  def redisClient() = {
    new RedisClient(
      configuration.get[String]("agile-engine.redis.host"),
      configuration.get[Int]("agile-engine.redis.port")
    )
  }
}
