package io.dantesinferno.game

import me.shadaj.slinky.core.Component
import me.shadaj.slinky.core.annotations.react
import me.shadaj.slinky.core.facade.ReactElement

case class GroundState(y: Double, x: Double = -1000000D, width: Double = 2000000D) extends CollidingObjectState[GroundState] { self =>
  val collisionGeometry = new CollisionBox[GroundState] {
    override def left: Double = x
    override def bottom: Double = y - 400
    override def right: Double = x + self.width
    override def top: Double = y

    override def state = self
    override def transform(newLeft: Double, newBottom: Double): GroundState = self
    override def markOnGround(onGround: Boolean): GroundState = self
  }

  override def render(tick: Int, windowX: Double): ReactElement = {
    Ground(this)
  }
}

@react class Ground extends Component {
  type Props = GroundState

  override def render(): ReactElement = {
    Group(x = props.x, y = 450 - props.y)(
      Rect(
        x = 0, y = 0,
        width = props.collisionGeometry.width, height = props.collisionGeometry.height,
        fill = "gray"
      )
    )
  }
}
