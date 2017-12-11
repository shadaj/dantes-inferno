package io.dantesinferno.game

import me.shadaj.slinky.core.Component
import me.shadaj.slinky.core.annotations.react
import me.shadaj.slinky.core.facade.ReactElement

case class StaticBoxState(x: Double, y: Double, width: Double, height: Double) extends CollidingObjectState[StaticBoxState] { self =>
  val collisionGeometry = new CollisionBox[StaticBoxState] {
    override def left: Double = x
    override def bottom: Double = y
    override def right: Double = x + self.width
    override def top: Double = y + self.height

    override def state = self
    override def transform(newLeft: Double, newBottom: Double): StaticBoxState = self
    override def markOnGround(onGround: Boolean): StaticBoxState = self
  }

  override def render(tick: Int, windowX: Double): ReactElement = {
    StaticBox(this)
  }
}

@react class StaticBox extends Component {
  type Props = StaticBoxState

  override def render(): ReactElement = {
    Group(x = props.x, y = (450 - props.y) - props.collisionGeometry.height)(
      Rect(
        x = 0, y = 0,
        width = props.collisionGeometry.width, height = props.collisionGeometry.height,
        fill = "brown"
      )
    )
  }
}
