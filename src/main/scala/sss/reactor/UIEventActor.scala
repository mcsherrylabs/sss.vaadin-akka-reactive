package sss.ui.reactor

import akka.actor.{Actor, ActorLogging, ActorRef}
import com.vaadin.ui.UI

import scala.util.{Failure, Success, Try}

abstract class UIEventActor extends Actor with ActorLogging {

  final def receive = setup

  private def setup: Receive = {
    case UIEventActorSetup(reactor: ActorRef, eventBroadcastEndpoint: ActorRef, ui: UI) =>
      context.become(commonHandler(reactor, eventBroadcastEndpoint, ui) orElse react(reactor, eventBroadcastEndpoint, ui))

    case x => log.warning(s"Not set up yet, punting $x")
      self forward x

  }

  private def commonHandler(reactor: ActorRef, eventBroadcastEndpoint: ActorRef, ui: UI): Receive = {

    case l: ListenTo => reactor ! l
    case s: StopListeningTo => reactor ! s

    case Push(f) => Try(ui.access(new Runnable {
      def run = Try { f() } match {
        case Failure(x) => log.error(x, "Failed to push changes to ui!")
        case Success(_) =>
      }
    })) match {
      case Failure(e) => log.error(e, "Failed to access ui from worker thread!")
      case Success(_) =>
    }

    case r @ Register(eventCategory) => eventBroadcastEndpoint ! r
    case r @ UnRegister(eventCategory) => eventBroadcastEndpoint ! r
    case Detach =>
      reactor ! Detach
      eventBroadcastEndpoint ! Detach
      context.become(setup)
  }

  def push(f: => Unit) = self ! Push(f _)

  def react(reactor: ActorRef, broadcaster: ActorRef, ui: UI): Receive

}
