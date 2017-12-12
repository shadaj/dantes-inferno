package io.dantesinferno.game

import me.shadaj.slinky.core.Component
import me.shadaj.slinky.core.annotations.react
import me.shadaj.slinky.core.facade.{Fragment, ReactElement}
import org.scalajs.dom
import org.scalajs.dom.html.Image
import org.scalajs.dom.raw.HTMLImageElement

case class MinotaurState(x: Double, y: Double,
                         spawnDantePosition: Double,
                         maxRight: Double,
                         hasSpawned: Boolean = false,
                         currentQuote: Option[String] = None) extends CollidingObjectState[MinotaurState] with WithQuotes[MinotaurState] { self =>
  val collisionGeometry = new CollisionBox[MinotaurState] {
    override def left: Double = if (hasSpawned) x else -100
    override def bottom: Double = if (hasSpawned) y else -100
    override def right: Double = if (hasSpawned) x + (68 * 4) else -100
    override def top: Double = if (hasSpawned) y + (46 * 4) else -100

    override def state = self

    override def transform(newLeft: Double, newBottom: Double): MinotaurState = {
      if (hasSpawned) copy(x = newLeft, y = newBottom) else self
    }

    override def markOnGround(onGround: Boolean): MinotaurState = self
  }

  override def setQuote(quote: Option[String]): MinotaurState = copy(currentQuote = quote)

  def superUpdate(worldState: WorldState) = super.update(worldState.copy(
    objects = worldState.objects.filterNot(_.isInstanceOf[DanteState])
  ))

  override def update(worldState: WorldState): MinotaurState = {
    val danteLocation = worldState.objects.find(_.isInstanceOf[DanteState]).get.asInstanceOf[DanteState]
    if (hasSpawned) {
      val physicsThenMove = copy(
        x = {
          if (collisionGeometry.right >= maxRight) x
          else if (danteLocation.x > (x + collisionGeometry.width / 2) + 10)
            x + 2
          else if (danteLocation.x < (x + collisionGeometry.width / 2) - 10)
            x - 2
          else x
        }
      )

      physicsThenMove.superUpdate(worldState.copy(
        objects = worldState.objects.map { obj =>
          if (obj == this) physicsThenMove else obj
        }
      ))
    } else {
      if (danteLocation.x >= spawnDantePosition) {
        copy(hasSpawned = true)
      } else {
        this
      }
    }
  }

  override def render(tick: Int, windowX: Double): ReactElement = {
    Minotaur(Minotaur.Props(this, tick))
  }
}

@react class Minotaur extends Component {
  case class Props(ds: MinotaurState, tick: Int)
  case class State(danteImage: Option[Image], facingRight: Boolean, moving: Boolean)

  override def initialState: State = State(None, facingRight = true, moving = false)

  override def componentDidMount(): Unit = {
    val image = dom.document.createElement("img").asInstanceOf[HTMLImageElement]
    image.onload = e => {
      setState(state.copy(danteImage = Some(image)))
    }

    image.src = "/minotaur.png"
  }

  override def componentWillReceiveProps(nextProps: Props): Unit = {
    setState(state.copy(
      facingRight =
        if (nextProps.ds.x >= props.ds.x + 1) true
        else if (nextProps.ds.x <= props.ds.x - 1) false
        else state.facingRight,
      moving = math.abs(nextProps.ds.x - props.ds.x) > 0.1
    ))
  }

  override def render(): ReactElement = {
    val spriteWidth = 68
    val spriteHeight = 46

    Group(x = props.ds.x, y = (450 - props.ds.y) - props.ds.collisionGeometry.height)(
      if (props.ds.hasSpawned) {
        Fragment(
          if (state.danteImage.isDefined) {
            Image(
              image = state.danteImage.get,
              x = 0, y = 0,
              width = props.ds.collisionGeometry.width, height = props.ds.collisionGeometry.height,
              crop = Some(Crop(
                x = if (state.moving) spriteWidth * ((props.tick / 10) % 4) else 0,
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
          },
          props.ds.currentQuote.map { quote =>
            Text(
              x = 20, y = -50,
              width = 300,
              text = quote,
              fontSize = 20, fontFamily = "Times",
              fill = "black"
            ): ReactElement
          }.getOrElse(Fragment())
        )
      } else {
        Fragment()
      }
    )
  }
}
