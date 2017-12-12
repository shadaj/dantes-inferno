package io.dantesinferno.game

import me.shadaj.slinky.core.Component
import me.shadaj.slinky.core.annotations.react
import me.shadaj.slinky.core.facade.{Fragment, ReactElement}
import org.scalajs.dom
import org.scalajs.dom.html.Image
import org.scalajs.dom.raw.HTMLImageElement

case class RiverOfBloodBackdropState(x: Double, y: Double, width: Double, height: Double) extends ObjectState[RiverOfBloodBackdropState] { self =>
  override def update(worldState: WorldState): RiverOfBloodBackdropState = self

  override def render(tick: Int, windowX: Double): ReactElement = {
    RiverOfBloodBackdrop(this, tick)
  }
}

@react class RiverOfBloodBackdrop extends Component {
  case class Props(rs: RiverOfBloodBackdropState, tick: Int)
  case class State(framesImages: List[Option[Image]])

  override def initialState: State = State(List.fill(10)(None))

  override def componentDidMount(): Unit = {
    (0 until 10).map { i =>
      val image = dom.document.createElement("img").asInstanceOf[HTMLImageElement]
      image.onload = e => {
        setState(s => s.copy(framesImages = s.framesImages.updated(i, Some(image))))
      }

      image.src = s"/river_of_blood/$i.png"
    }
  }

  override def render(): ReactElement = {
    val curImage = state.framesImages((props.tick / 10) % 10)
    Group(x = props.rs.x, y = (450 - props.rs.y) - props.rs.height)(
      if (curImage.isDefined) {
        Rect(
          x = 0, y = 0,
          width = props.rs.width, height = props.rs.height,
          fillPatternImage = curImage.get,
          fillPatternRepeat = "repeat"
        )
      } else {
        Rect(
          x = 0, y = 0,
          width = props.rs.width, height = props.rs.height,
          fill = "yellow"
        )
      }
    )
  }
}
