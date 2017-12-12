package io.dantesinferno.game

import me.shadaj.slinky.core.Component
import me.shadaj.slinky.core.annotations.react
import me.shadaj.slinky.core.facade.{Fragment, ReactElement}
import org.scalajs.dom
import org.scalajs.dom.html.Image
import org.scalajs.dom.raw.HTMLImageElement

case class BackdropState(x: Double, y: Double, width: Double, height: Double, url: String) extends ObjectState[BackdropState] { self =>
  override def update(worldState: WorldState): BackdropState = self

  override def render(tick: Int, windowX: Double): ReactElement = {
    Backdrop(this)
  }
}

@react class Backdrop extends Component {
  type Props = BackdropState
  case class State(danteImage: Option[Image])

  override def initialState: State = State(None)

  override def componentDidMount(): Unit = {
    val image = dom.document.createElement("img").asInstanceOf[HTMLImageElement]
    image.onload = e => {
      setState(state.copy(danteImage = Some(image)))
    }

    image.src = props.url
  }

  override def render(): ReactElement = {
    Group(x = props.x, y = (450 - props.y) - props.height)(
      if (state.danteImage.isDefined) {
        Rect(
          x = 0, y = 0,
          width = props.width, height = props.height,
          fillPatternImage = state.danteImage.get,
          fillPatternRepeat = "repeat"
        )
      } else {
        Rect(
          x = 0, y = 0,
          width = props.width, height = props.height,
          fill = "yellow"
        )
      }
    )
  }
}
