package io.dantesinferno.game

import me.shadaj.slinky.core.Component
import me.shadaj.slinky.core.annotations.react
import me.shadaj.slinky.core.facade.{Fragment, ReactElement}
import org.scalajs.dom
import org.scalajs.dom.html.Image
import org.scalajs.dom.raw.HTMLImageElement

case class RiverOfBloodBackdropState(x: Double, y: Double) extends ObjectState[RiverOfBloodBackdropState] { self =>
  override def update(worldState: WorldState): RiverOfBloodBackdropState = self

  override def render(tick: Int, windowX: Double): ReactElement = {
    RiverOfBloodBackdrop(this)
  }
}

@react class RiverOfBloodBackdrop extends Component {
  type Props = RiverOfBloodBackdropState
  case class State(image: Option[Image])

  override def initialState: State = State(None)

  override def componentDidMount(): Unit = {
    val image = dom.document.createElement("img").asInstanceOf[HTMLImageElement]
    image.onload = e => {
      setState(s => s.copy(image = Some(image)))
    }

    image.src = s"/river_of_blood.png"
  }

  override def render(): ReactElement = {
    Group(x = props.x, y = 450 - props.y - 240 - 100)(
      if (state.image.isDefined) {
        Image(
          image = state.image.get,
          x = 0, y = 0,
          width = 640, height = 240
        )
      } else {
        Rect(
          x = 0, y = 0,
          width = 640, height = 240,
          fill = "yellow"
        )
      }
    )
  }
}
