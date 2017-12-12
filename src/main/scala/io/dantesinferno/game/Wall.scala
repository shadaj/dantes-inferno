package io.dantesinferno.game

import me.shadaj.slinky.core.Component
import me.shadaj.slinky.core.annotations.react
import me.shadaj.slinky.core.facade.ReactElement

case class WallState(left: Double, right: Double) extends CollidingObjectState[WallState] { self =>
  val collisionGeometry = new CollisionBox[WallState] {
    override def left: Double = self.left
    override def bottom: Double = 0D
    override def right: Double = self.right
    override def top: Double = 100000D

    override def state = self
    override def transform(newLeft: Double, newBottom: Double): WallState = self
    override def markOnGround(onGround: Boolean): WallState = self
  }

  override def render(tick: Int, windowX: Double): ReactElement = {
    Wall(this)
  }
}

@react class Wall extends Component {
  type Props = WallState

  override def render(): ReactElement = {
    Group(x = props.left, y = 0D)(
      Rect(
        x = 0, y = 450,
        width = props.collisionGeometry.width, height = -props.collisionGeometry.height,
        fill = "gray"
      )
    )
  }
}
