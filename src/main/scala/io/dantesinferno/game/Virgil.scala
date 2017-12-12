package io.dantesinferno.game

import me.shadaj.slinky.core.Component
import me.shadaj.slinky.core.annotations.react
import me.shadaj.slinky.core.facade.{Fragment, ReactElement}
import org.scalajs.dom
import org.scalajs.dom.html.Image
import org.scalajs.dom.raw.HTMLImageElement

case class VirgilState(x: Double, y: Double, xVel: Double, currentQuote: Option[String] = None) extends ObjectState[VirgilState] with WithQuotes[VirgilState] { self =>
  override def update(worldState: WorldState): VirgilState = {
    val danteLocation = worldState.objects.find(_.isInstanceOf[DanteState]).get.asInstanceOf[DanteState]
    self.copy(x = (x * 15 + danteLocation.x) / 16, y = (y * 15 + danteLocation.y) / 16, xVel = danteLocation.xVel)
  }

  override def setQuote(quote: Option[String]): VirgilState = {
    copy(currentQuote = quote)
  }

  override def render(tick: Int, windowX: Double): ReactElement = {
    Virgil(this, tick, windowX)
  }
}

@react class Virgil extends Component {
  case class Props(ds: VirgilState, tick: Int, windowX: Double)
  case class State(danteImage: Option[Image], facingRight: Boolean, currentX: Double)

  override def initialState: State = State(None, facingRight = true, props.ds.x)

  override def componentDidMount(): Unit = {
    val image = dom.document.createElement("img").asInstanceOf[HTMLImageElement]
    image.onload = e => {
      setState(state.copy(danteImage = Some(image)))
    }

    image.src = "/virgil.png"
  }

  override def componentWillReceiveProps(nextProps: Props): Unit = {
    import nextProps.ds.xVel
    val targetX = props.ds.x + (if (state.facingRight) -75 else 75)
    setState(state.copy(
      facingRight = if (xVel > 0) true else if (xVel < 0) false else state.facingRight,
      currentX = (state.currentX * 30 + targetX) / 31)
    )
  }

  override def render(): ReactElement = {
    val spriteWidth = 23
    val spriteHeight = 54

    val xWithBob = state.currentX - 10 * math.sin(props.tick.toDouble / 25)
    val yWithoutBob = (450 - props.ds.y) - (spriteHeight * 2) - 50
    val yWithBob = yWithoutBob + 10 * math.sin(props.tick.toDouble / 20)

    Group(x = xWithBob, y = yWithBob)(
      if (state.danteImage.isDefined) {
        Image(
          image = state.danteImage.get,
          x = 0, y = 0,
          width = spriteWidth * 2, height = spriteHeight * 2,
          crop = Some(Crop(
            x = 0,
            y = if (state.facingRight) 0 else spriteHeight,
            width = spriteWidth,
            height = spriteHeight
          ))
        )
      } else {
        Rect(
          x = 0, y = 0,
          width = spriteWidth * 2, height = spriteHeight * 2,
          fill = "yellow"
        )
      },
      props.ds.currentQuote.map { quote =>
        Text(
          x = -xWithBob + state.currentX + spriteWidth * 2, y = -yWithBob + yWithoutBob,
          width = 200,
          text = quote,
          fontSize = 20, fontFamily = "Times",
          fill = "white"
        ): ReactElement
      }.getOrElse(Fragment())
    )
  }
}
