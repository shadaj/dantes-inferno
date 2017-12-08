package io.dantesinferno.game

import me.shadaj.slinky.core.Component
import me.shadaj.slinky.core.annotations.react
import org.scalajs.dom
import org.scalajs.dom.raw.KeyboardEvent

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("resources/app.css", JSImport.Default)
@js.native
object AppCSS extends js.Object

@JSImport("resources/logo.svg", JSImport.Default)
@js.native
object ReactLogo extends js.Object

case class DanteState(x: Double, y: Double, yVel: Double, xVel: Double, xAcc: Double) {
  def update: DanteState = {
    copy(
      x = x + xVel,
      y = if (y < 0) 0 else y + yVel,
      yVel = if (y < 0) 0 else if (y > 0) yVel - 1.5 else yVel,
      xVel = (xVel + xAcc) * 0.6
    )
  }
}

@react class App extends Component {
  type Props = Unit

  case class State(danteState: DanteState)

  override def initialState: App.State = {
    State(danteState = DanteState(
      x = 0,
      y = 0,
      xVel = 0,
      yVel = 0,
      xAcc = 0
    ))
  }

  private val css = AppCSS

  def onKeyDown(key: KeyboardEvent): Unit = {
    key.key match {
      case "ArrowRight" => setState(state.copy(danteState = state.danteState.copy(xAcc = 2)))
      case "ArrowLeft" => setState(state.copy(danteState = state.danteState.copy(xAcc = -2)))
      case "ArrowUp" =>
        if (state.danteState.yVel == 0) { // TODO: fix this
          setState(state.copy(danteState = state.danteState.copy(yVel = 20)))
        }

      case o => println(s"Unhandled key! $o")
    }
  }

  def onKeyUp(key: KeyboardEvent): Unit = {
    key.key match {
      case "ArrowRight" => setState(state.copy(danteState = state.danteState.copy(xAcc = 0)))
      case "ArrowLeft" => setState(state.copy(danteState = state.danteState.copy(xAcc = 0)))
      case "ArrowUp" =>
      case o => println(s"Unhandled key! $o")
    }
  }

  def animateFrame(): Unit = {
    setState(state.copy(danteState = state.danteState.update))

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

    animateFrame()
  }

  override def shouldComponentUpdate(nextProps: Unit, nextState: App.State): Boolean = {
    props != nextProps || state != nextState
  }

  override def componentDidUpdate(prevProps: Props, prevState: State): Unit = {
    dom.document.onkeydown = key => {
      onKeyDown(key)
    }

    dom.document.onkeyup = key => {
      onKeyUp(key)
    }
  }

  def render() = {
    Stage(width = 800, height = 450)(
      Layer(
        Dante(state.danteState)
      )
    )
  }
}
