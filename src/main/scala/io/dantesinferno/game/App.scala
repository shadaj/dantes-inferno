package io.dantesinferno.game

import me.shadaj.slinky.core.Component
import me.shadaj.slinky.core.annotations.react
import me.shadaj.slinky.web.html._
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

case class WorldState(objects: List[ObjectState[_]], windowX: Double, tick: Int)

@react class App extends Component {
  type Props = Unit

  type State = WorldState

  override def initialState: App.State = {
    WorldState(
      List(
        DanteState(
          0, 0, 0, 0, 0, true
        ),
        StaticBoxState(
          75, 0
        )
      ),
      0,
      0
    )
  }

  private val css = AppCSS

  def onKeyDown(key: KeyboardEvent): Unit = {
    key.key match {
      case "ArrowRight" => setState(state.copy(objects = state.objects.head.asInstanceOf[DanteState].copy(xAcc = 2) :: state.objects.tail))
      case "ArrowLeft" => setState(state.copy(objects = state.objects.head.asInstanceOf[DanteState].copy(xAcc = -2) :: state.objects.tail))
      case "ArrowUp" =>
        if (state.objects.head.asInstanceOf[DanteState].onGround) { // TODO: fix this
          setState(state.copy(objects = state.objects.head.asInstanceOf[DanteState].copy(yVel = 20) :: state.objects.tail))
        }

      case o => println(s"Unhandled key! $o")
    }
  }

  def onKeyUp(key: KeyboardEvent): Unit = {
    key.key match {
      case "ArrowRight" => setState(state.copy(objects = state.objects.head.asInstanceOf[DanteState].copy(xAcc = 0) :: state.objects.tail))
      case "ArrowLeft" => setState(state.copy(objects = state.objects.head.asInstanceOf[DanteState].copy(xAcc = 0) :: state.objects.tail))
      case "ArrowUp" =>
      case o => println(s"Unhandled key! $o")
    }
  }

  def animateFrame(): Unit = {
    val danteState = state.objects.head.asInstanceOf[DanteState]

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

    animateFrame()
  }

  override def shouldComponentUpdate(nextProps: Unit, nextState: State): Boolean = {
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
    div(style := js.Dynamic.literal(width = "800px", height = "100vh", marginLeft = "auto", marginRight = "auto", display = "flex", alignItems = "center"))(
      Stage(width = 800, height = 450)(
        Layer(x = -state.windowX)(
          state.objects.map(_.render(state.tick))
        )
      )
    )
  }
}
