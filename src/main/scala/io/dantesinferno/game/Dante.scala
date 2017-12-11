package io.dantesinferno.game

import me.shadaj.slinky.core.Component
import me.shadaj.slinky.core.annotations.react
import me.shadaj.slinky.core.facade.ReactElement
import org.scalajs.dom
import org.scalajs.dom.html.Image
import org.scalajs.dom.raw.HTMLImageElement

case class DanteState(x: Double, y: Double,
                      yVel: Double = 0, xVel: Double = 0,
                      xAcc: Double = 0,onGround: Boolean = true) extends CollidingObjectState[DanteState] { self =>
  val collisionGeometry = new CollisionBox[DanteState] {
    override def left: Double = x
    override def bottom: Double = y
    override def right: Double = x + (28 * 2)
    override def top: Double = y + (56 * 2)

    override def state = self

    override def transform(newLeft: Double, newBottom: Double): DanteState = {
      copy(
        x = newLeft,
        y = newBottom,
        yVel = if (yVel < 0 && newBottom > bottom) 0 else yVel
      )
    }

    override def markOnGround(onGround: Boolean): DanteState = {
      copy(onGround = onGround)
    }
  }

  def superUpdate(worldState: WorldState): DanteState = super.update(worldState)

  override def update(worldState: WorldState): DanteState = {
    val superUpdated = super.update(worldState)

    superUpdated.copy(
      x = superUpdated.x + superUpdated.xVel,
      y = superUpdated.y + superUpdated.yVel,
      yVel = if (!superUpdated.onGround) superUpdated.yVel - 1.5 else superUpdated.yVel,
      xVel = (superUpdated.xVel + superUpdated.xAcc) * 0.6
    )
  }

  override def render(tick: Int): ReactElement = {
    Dante(Dante.Props(this, tick))
  }
}

@react class Dante extends Component {
  case class Props(ds: DanteState, tick: Int)
  case class State(danteImage: Option[Image], facingRight: Boolean)

  override def initialState: State = State(None, facingRight = true)

  override def componentDidMount(): Unit = {
    val image = dom.document.createElement("img").asInstanceOf[HTMLImageElement]
    image.onload = e => {
      setState(state.copy(danteImage = Some(image)))
    }

    image.src = "/dante_2.png"
  }

  override def componentWillReceiveProps(nextProps: Props): Unit = {
    import nextProps.ds.xVel
    setState(state.copy(facingRight = if (xVel > 0) true else if (xVel < 0) false else state.facingRight))
  }

  override def render(): ReactElement = {
    val spriteWidth = 28
    val spriteHeight = 54

    Group(x = props.ds.x, y = (450 - props.ds.y) - props.ds.collisionGeometry.height)(
      if (state.danteImage.isDefined) {
        Image(
          image = state.danteImage.get,
          x = 0, y = 0,
          width = props.ds.collisionGeometry.width, height = props.ds.collisionGeometry.height,
          crop = Some(Crop(
            x = if (math.abs(props.ds.xVel) > 0.1) spriteWidth * ((props.tick / 5) % 4) else 0,
            y = if (state.facingRight) 0 else spriteHeight,
            width = spriteWidth,
            height = spriteHeight
          ))
        )
      } else {
        Rect(
          x = 0, y = 0,
          width = props.ds.collisionGeometry.width, height = props.ds.collisionGeometry.height,
          fill = "yellow"
        )
      }
    )
  }
}
