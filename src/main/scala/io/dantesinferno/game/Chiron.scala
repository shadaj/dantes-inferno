package io.dantesinferno.game

import me.shadaj.slinky.core.Component
import me.shadaj.slinky.core.annotations.react
import me.shadaj.slinky.core.facade.{Fragment, ReactElement}
import org.scalajs.dom
import org.scalajs.dom.html.Image
import org.scalajs.dom.raw.HTMLImageElement

case class ChironState(x: Double, y: Double,
                       danteOnLeft: Boolean = true,
                       currentQuote: Option[String] = None) extends ObjectState[ChironState] with WithQuotes[ChironState] { self =>
  override def setQuote(quote: Option[String]): ChironState = copy(currentQuote = quote)

  override def update(worldState: WorldState): ChironState = {
    val danteLocation = worldState.objects.find(_.isInstanceOf[DanteState]).get.asInstanceOf[DanteState]
    if (danteLocation.x < x) copy(danteOnLeft = true) else copy(danteOnLeft = false)
  }

  override def render(tick: Int, windowX: Double): ReactElement = {
    Chiron(this)
  }
}

@react class Chiron extends Component {
  type Props = ChironState
  case class State(danteImage: Option[Image])

  override def initialState: State = State(None)

  override def componentDidMount(): Unit = {
    val image = dom.document.createElement("img").asInstanceOf[HTMLImageElement]
    image.onload = e => {
      setState(state.copy(danteImage = Some(image)))
    }

    image.src = "/chiron.png"
  }

  override def render(): ReactElement = {
    val spriteWidth = 79
    val spriteHeight = 94

    Group(x = props.x, y = (450 - props.y) - (spriteHeight * 2))(
      if (state.danteImage.isDefined) {
        Image(
          image = state.danteImage.get,
          x = 0, y = 0,
          width = (spriteWidth * 2), height = (spriteHeight * 2),
          crop = Some(Crop(
            x = 0,
            y = (if (props.danteOnLeft) 1 else 0) * spriteHeight,
            width = spriteWidth,
            height = spriteHeight
          ))
        )
      } else {
        Rect(
          x = 0, y = 0,
          width = (spriteWidth * 2), height = (spriteHeight * 2),
          fill = "yellow"
        )
      },
      props.currentQuote.map { quote =>
        Text(
          x = 80, y = 0,
          width = 200,
          text = quote,
          fontSize = 20, fontFamily = "Times",
          fill = "white"
        ): ReactElement
      }.getOrElse(Fragment())
    )
  }
}
