package io.dantesinferno.game

import me.shadaj.slinky.core.Component
import me.shadaj.slinky.core.annotations.react
import me.shadaj.slinky.core.facade.Fragment
import me.shadaj.slinky.web.html._
import org.scalajs.dom
import org.scalajs.dom.raw.{HTMLCanvasElement, KeyboardEvent}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

case class WorldState(objects: List[ObjectState[_]],
                      triggeredQuotes: List[(Double, (List[(String, String, Option[Double], Option[Long])]))],
                      windowX: Double = 0,
                      windowY: Double = 0,
                      queuedQuotes: List[(String, String, Option[Double], Option[Long])] = List.empty,
                      animatingWindowX: Double = 0,
                      animatingWindowY: Double = 0,
                      tick: Int = 0,
                      currentQuote: Option[((String, String, Option[Double], Option[Long]), Long)] = None)

@react class World extends Component {
  type Props = WorldState

  type State = WorldState

  override def initialState: State = {
    props
  }

  private val css = AppCSS

  def danteState: DanteState = state.objects.find(_.isInstanceOf[DanteState]).get.asInstanceOf[DanteState]
  def virgilState: VirgilState = state.objects.find(_.isInstanceOf[VirgilState]).get.asInstanceOf[VirgilState]
  def updateDanteState(transform: DanteState => DanteState) = state.copy(objects = state.objects.map {
    case s: DanteState => transform(s)
    case o => o
  })
  def updateVirgilState(transform: VirgilState => VirgilState) = state.copy(objects = state.objects.map {
    case s: VirgilState => transform(s)
    case o => o
  })

  def stateWithQuoteAdvanced(stateToTransform: WorldState): WorldState = {
    if (stateToTransform.queuedQuotes.nonEmpty) {
      stateToTransform.copy(
        objects = stateToTransform.objects.map { obj =>
          if (stateToTransform.currentQuote.exists(_._1._1 == obj.getClass.getSimpleName)) {
            obj.asInstanceOf[WithQuotes[_]].setQuote(None).asInstanceOf[ObjectState[_]]
          } else {
            obj
          }
        }.map { obj =>
          if (stateToTransform.queuedQuotes.head._1 == obj.getClass.getSimpleName) {
            obj.asInstanceOf[WithQuotes[_]].setQuote(Some(stateToTransform.queuedQuotes.head._2)).asInstanceOf[ObjectState[_]]
          } else {
            obj
          }
        },
        queuedQuotes = stateToTransform.queuedQuotes.tail,
        currentQuote = Some(stateToTransform.queuedQuotes.head, System.currentTimeMillis())
      )
    } else {
      stateToTransform.copy(
        objects = stateToTransform.objects.map { obj =>
          if (stateToTransform.currentQuote.exists(_._1._1 == obj.getClass.getSimpleName)) {
            obj.asInstanceOf[WithQuotes[_]].setQuote(None).asInstanceOf[ObjectState[_]]
          } else {
            obj
          }
        },
        queuedQuotes = List.empty,
        currentQuote = None
      )
    }
  }

  def onKeyDown(key: KeyboardEvent): Unit = {
    val quoteAllowsMove = state.currentQuote.isEmpty || state.currentQuote.exists(_._1._3.isEmpty)

    key.key match {
      case "ArrowRight" =>
        if ((state.queuedQuotes.nonEmpty || state.currentQuote.isDefined) && state.currentQuote.get._1._4.isEmpty) {
          setState(stateWithQuoteAdvanced(state))
        } else if (quoteAllowsMove) {
          setState(updateDanteState(_.copy(xAcc = 2)))
        }
      case "ArrowLeft" =>
        if (quoteAllowsMove) {
          setState(updateDanteState(_.copy(xAcc = -2)))
        }

      case "ArrowUp" =>
        if (quoteAllowsMove && danteState.onGround) {
          setState(updateDanteState(_.copy(yVel = 20)))
        }

      case o => println(s"Unhandled key! $o")
    }
  }

  def onKeyUp(key: KeyboardEvent): Unit = {
    key.key match {
      case "ArrowRight" => setState(updateDanteState(_.copy(xAcc = 0)))
      case "ArrowLeft" => setState(updateDanteState(_.copy(xAcc = 0)))
      case "ArrowUp" =>
      case o => println(s"Unhandled key! $o")
    }
  }

  def animateFrame(): Unit = {
    val windowTriggerBorderX = 200
    val newWindowX = if (danteState.x > state.windowX + (800 - windowTriggerBorderX - 50)) {
      danteState.x - (800 - windowTriggerBorderX - 50)
    } else if (danteState.x < state.windowX + windowTriggerBorderX) {
      danteState.x - windowTriggerBorderX
    } else {
      state.windowX
    }

    val windowTriggerBorderY = 100
    val newWindowY = if (danteState.y > state.windowY + (450 - windowTriggerBorderY - 50)) {
      danteState.y - (450 - windowTriggerBorderY - 50)
    } else if (danteState.y < state.windowY + windowTriggerBorderY) {
      danteState.y - windowTriggerBorderY
    } else {
      state.windowY
    }

    val stateQueuedQuotesUpdated = if (state.queuedQuotes.isEmpty && state.triggeredQuotes.headOption.exists(danteState.x >= _._1)) {
      state.copy(
        queuedQuotes = state.queuedQuotes ++ state.triggeredQuotes.head._2,
        triggeredQuotes = state.triggeredQuotes.tail
      )
    } else {
      state
    }

    val stateQuotesUpdated = if (stateQueuedQuotesUpdated.queuedQuotes.nonEmpty && stateQueuedQuotesUpdated.currentQuote.isEmpty) {
      stateQueuedQuotesUpdated.copy(
        objects = stateQueuedQuotesUpdated.objects.map { obj =>
          if (stateQueuedQuotesUpdated.queuedQuotes.head._1 == obj.getClass.getSimpleName) {
            obj.asInstanceOf[WithQuotes[_]].setQuote(Some(stateQueuedQuotesUpdated.queuedQuotes.head._2)).asInstanceOf[ObjectState[_]]
          } else {
            obj
          }
        }.map {
          case s: DanteState => s.copy(xAcc = 0, xVel = 0)
          case o => o
        },
        queuedQuotes = stateQueuedQuotesUpdated.queuedQuotes.tail,
        currentQuote = Some(stateQueuedQuotesUpdated.queuedQuotes.head, System.currentTimeMillis())
      )
    } else stateQueuedQuotesUpdated

    val withAutoAdvance = if (stateQuotesUpdated.currentQuote.exists(quote => quote._1._4.exists(timeout => (System.currentTimeMillis() - quote._2) > timeout))) {
      stateWithQuoteAdvanced(stateQuotesUpdated)
    } else stateQuotesUpdated

    val withWindowPosition = withAutoAdvance.copy(
      windowX = if (withAutoAdvance.currentQuote.isDefined) withAutoAdvance.currentQuote.get._1._3.getOrElse(newWindowX) else newWindowX,
      windowY = newWindowY
    )

    setState(withWindowPosition.copy(
      objects = withWindowPosition.objects.map(_.update(state).asInstanceOf[ObjectState[_]]),
      animatingWindowX = (withWindowPosition.animatingWindowX * 15 + withWindowPosition.windowX) / 16,
      animatingWindowY = (withWindowPosition.animatingWindowY * 15 + withWindowPosition.windowY) / 16,
      tick = withWindowPosition.tick + 1
    ))

    dom.window.requestAnimationFrame(something => {
      animateFrame()
    })
  }

  override def componentDidMount(): Unit = {
    dom.document.onkeydown = key => {
      onKeyDown(key)
    }

    dom.document.onkeyup = key => {
      onKeyUp(key)
    }

    val canvasContext = dom.document.getElementsByClassName("konvajs-content")(0).firstChild.asInstanceOf[HTMLCanvasElement].getContext("2d")
    canvasContext.imageSmoothingEnabled = false

    animateFrame()
  }

  override def shouldComponentUpdate(nextProps: Props, nextState: State): Boolean = {
    props != nextProps || state != nextState
  }

  def render() = {
    Layer(x = -state.animatingWindowX, y = state.windowY)(
      state.objects.zipWithIndex.map { case (obj, index) =>
        Fragment.withKey(index.toString)(obj.render(state.tick, state.windowX))
      }
    )
  }
}
