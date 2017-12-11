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
                      queuedQuotes: List[(Class[_], String)],
                      windowX: Double = 0, tick: Int = 0,
                      currentQuote: Option[(Class[_], String)] = None)

@react class World extends Component {
  type Props = WorldState

  type State = WorldState

  override def initialState: State = {
    if (props.queuedQuotes.nonEmpty) {
      props.copy(
        objects = props.objects.map { obj =>
          if (props.currentQuote.exists(_._1.isInstance(obj))) {
            obj.asInstanceOf[WithQuotes[_]].setQuote(None).asInstanceOf[ObjectState[_]]
          } else {
            obj
          }
        }.map { obj =>
          if (props.queuedQuotes.head._1.isInstance(obj)) {
            obj.asInstanceOf[WithQuotes[_]].setQuote(Some(props.queuedQuotes.head._2)).asInstanceOf[ObjectState[_]]
          } else {
            obj
          }
        },
        queuedQuotes = props.queuedQuotes.tail,
        currentQuote = Some(props.queuedQuotes.head)
      )
    } else {
      props
    }
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

  def onKeyDown(key: KeyboardEvent): Unit = {
    key.key match {
      case "ArrowRight" =>
        if (state.queuedQuotes.nonEmpty) {
          setState(state.copy(
            objects = state.objects.map { obj =>
              if (state.currentQuote.exists(_._1.isInstance(obj))) {
                obj.asInstanceOf[WithQuotes[_]].setQuote(None).asInstanceOf[ObjectState[_]]
              } else {
                obj
              }
            }.map { obj =>
              if (state.queuedQuotes.head._1.isInstance(obj)) {
                obj.asInstanceOf[WithQuotes[_]].setQuote(Some(state.queuedQuotes.head._2)).asInstanceOf[ObjectState[_]]
              } else {
                obj
              }
            },
            queuedQuotes = state.queuedQuotes.tail,
            currentQuote = Some(state.queuedQuotes.head)
          ))
        } else if (state.currentQuote.isDefined) {
          setState(state.copy(
            objects = state.objects.map { obj =>
              if (state.currentQuote.exists(_._1.isInstance(obj))) {
                obj.asInstanceOf[WithQuotes[_]].setQuote(None).asInstanceOf[ObjectState[_]]
              } else {
                obj
              }
            },
            queuedQuotes = List.empty,
            currentQuote = None
          ))
        } else {
          setState(updateDanteState(_.copy(xAcc = 2)))
        }
      case "ArrowLeft" =>
        if (state.currentQuote.isDefined) {
        } else {
          setState(updateDanteState(_.copy(xAcc = -2)))
        }

      case "ArrowUp" =>
        if (state.currentQuote.isDefined) {
        } else if (danteState.onGround) {
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
    setState(state.copy(
      objects = state.objects.map(_.update(state).asInstanceOf[ObjectState[_]]),
      windowX = if (danteState.x > state.windowX + (800 - 10 - 50)) {
        danteState.x - (800 - 10 - 50)
      } else if (danteState.x < state.windowX + 10) {
        danteState.x - 10
      } else {
        state.windowX
      },
      tick = state.tick + 1
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
    Layer(x = -state.windowX)(
      state.objects.zipWithIndex.map { case (obj, index) =>
        Fragment.withKey(index.toString)(obj.render(state.tick, state.windowX))
      }
    )
  }
}
