package io.dantesinferno.game

import me.shadaj.slinky.core.Component
import me.shadaj.slinky.core.annotations.react
import me.shadaj.slinky.core.facade.ReactElement

case class GroundState(y: Double) extends CollidingObjectState[GroundState] { self =>
  val collisionGeometry = new CollisionBox[GroundState] {
    override def left: Double = -1000000D
    override def bottom: Double = y
    override def right: Double = 1000000D
    override def top: Double = y + 50

    override def state = self
    override def transform(newLeft: Double, newBottom: Double): GroundState = self
    override def markOnGround(onGround: Boolean): GroundState = self
  }

  override def render(tick: Int): ReactElement = {
    Ground(this)
  }
}

@react class Ground extends Component {
  type Props = GroundState

  override def render(): ReactElement = {
    Group(x = -1000000D, y = (450 - props.y) - props.collisionGeometry.height)(
      Rect(
        x = 0, y = 0,
        width = props.collisionGeometry.width, height = props.collisionGeometry.height,
        fill = "gray"
      )
    )
  }
}
