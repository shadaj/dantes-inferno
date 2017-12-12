package io.dantesinferno.game

import me.shadaj.slinky.core.Component
import me.shadaj.slinky.core.annotations.react
import me.shadaj.slinky.core.facade.{Fragment, ReactElement}
import org.scalajs.dom
import org.scalajs.dom.html.Image
import org.scalajs.dom.raw.HTMLImageElement

case class CentaurState(x: Double, y: Double,
                        spawnDantePosition: Double,
                        stopMovingDantePosition: Double,
                        relaxDantePosition: Double,
                        hasSpawned: Boolean = false,
                        isRelaxed: Boolean = false,
                        currentQuote: Option[String] = None) extends CollidingObjectState[CentaurState] with WithQuotes[CentaurState] { self =>
  val collisionGeometry = new CollisionBox[CentaurState] {
    override def left: Double = if (hasSpawned) x else -100
    override def bottom: Double = if (hasSpawned) y else -100
    override def right: Double = if (hasSpawned) x + 94 else -100
    override def top: Double = if (hasSpawned) y + 94 else -100

    override def state = self

    override def transform(newLeft: Double, newBottom: Double): CentaurState = {
      if (hasSpawned) copy(x = newLeft, y = newBottom) else self
    }

    override def markOnGround(onGround: Boolean): CentaurState = self
  }

  override def setQuote(quote: Option[String]): CentaurState = copy(currentQuote = quote)

  def superUpdate(worldState: WorldState) = super.update(worldState.copy(
    objects = worldState.objects.filterNot(_.isInstanceOf[DanteState])
  ))

  override def update(worldState: WorldState): CentaurState = {
    val danteLocation = worldState.objects.find(_.isInstanceOf[DanteState]).get.asInstanceOf[DanteState]
    if (hasSpawned) {
      if (!isRelaxed && danteLocation.x >= relaxDantePosition) {
        copy(isRelaxed = true)
      } else {
        val nextMinotaur = worldState.objects.filter(_.isInstanceOf[CentaurState]).map(_.asInstanceOf[CentaurState])
          .map(_.collisionGeometry.left).filter(_ > collisionGeometry.right).sorted.headOption

        val targetPoint = nextMinotaur.getOrElse(danteLocation.collisionGeometry.left)

        val physicsThenMove = copy(
          x = {
            if (danteLocation.x >= stopMovingDantePosition) x
            else if (targetPoint > collisionGeometry.right + 5)
              x + 2
            else if (targetPoint < collisionGeometry.left - 5)
              x - 2
            else x
          },
          y = y - 0.2
        )

        physicsThenMove.superUpdate(worldState.copy(
          objects = worldState.objects.map { obj =>
            if (obj == this) physicsThenMove else obj
          }
        ))
      }
    } else {
      if (danteLocation.x >= spawnDantePosition) {
        copy(hasSpawned = true)
      } else {
        this
      }
    }
  }

  override def render(tick: Int, windowX: Double): ReactElement = {
    Centaur(Centaur.Props(this, tick))
  }
}

@react class Centaur extends Component {
  case class Props(ds: CentaurState, tick: Int)
  case class State(danteImage: Option[Image], facingRight: Boolean, moving: Boolean)

  override def initialState: State = State(None, facingRight = true, moving = false)

  override def componentDidMount(): Unit = {
    val image = dom.document.createElement("img").asInstanceOf[HTMLImageElement]
    image.onload = e => {
      setState(state.copy(danteImage = Some(image)))
    }

    image.src = "/centaur.png"
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
    val spriteWidth = 94
    val spriteHeight = 94

    Group(x = props.ds.x, y = (450 - props.ds.y) - props.ds.collisionGeometry.height)(
      if (props.ds.hasSpawned) {
        Fragment(
          if (state.danteImage.isDefined) {
            val imageRow = (if (state.moving) 4 else 0) + (if (state.facingRight) 0 else 2) + (if (props.ds.isRelaxed) 1 else 0)
            Image(
              image = state.danteImage.get,
              x = 0, y = 0,
              width = props.ds.collisionGeometry.width, height = props.ds.collisionGeometry.height,
              crop = Some(Crop(
                x = if (state.moving) spriteWidth * ((props.tick / 10) % 4) else 0,
                y = imageRow * spriteHeight,
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
              x = 20, y = -100,
              width = props.ds.collisionGeometry.width,
              text = quote,
              fontSize = 20, fontFamily = "Times",
              fill = "white"
            ): ReactElement
          }.getOrElse(Fragment())
        )
      } else {
        Fragment()
      }
    )
  }
}
