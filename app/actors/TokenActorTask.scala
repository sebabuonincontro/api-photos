package actors

import java.util.concurrent.TimeUnit

import actors.TokenActor.ObtainList
import akka.actor.{ActorRef, ActorSystem}
import javax.inject.{Inject, Named}
import play.api.Configuration

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

/***
 * Scheduler for obtain information of the Api.
 * @param actorSystem
 * @param tokenActor
 * @param ec
 */
class TokenActorTask @Inject()(
  actorSystem: ActorSystem,
  @Named("token-actor")tokenActor: ActorRef,
  configuration: Configuration)(implicit ec: ExecutionContext) {

  val interval = configuration.get[Int]("agile-engine.intervals")

  actorSystem.scheduler.scheduleAtFixedRate(
    initialDelay = 0.microseconds,
    interval = Duration(interval, TimeUnit.HOURS),
    receiver = tokenActor,
    message = ObtainList()
  )
}
