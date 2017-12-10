package io.dantesinferno.game

import me.shadaj.slinky.core.Component
import me.shadaj.slinky.core.annotations.react
import me.shadaj.slinky.core.facade.ReactElement

case class DanteState(x: Double, y: Double, yVel: Double, xVel: Double, xAcc: Double, onGround: Boolean) extends CollidingObjectState[DanteState] { self =>
  val collisionGeometry = new CollisionBox[DanteState] {
    override def left: Double = x
    override def bottom: Double = y
    override def right: Double = x + 50
    override def top: Double = y + 100

    override def state = self

    override def transform(newLeft: Double, newBottom: Double): DanteState = {
      copy(
        x = newLeft,
        y = newBottom,
        yVel = if (yVel < 0 && newBottom >= bottom) 0 else yVel
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

  override def render: ReactElement = {
    Dante(this)
  }
}

@react class Dante extends Component {
  type Props = DanteState

  override def render(): ReactElement = {
    Group(x = props.x, y = (450 - props.y) - props.collisionGeometry.height)(
      Rect(
        x = 0, y = 0,
        width = props.collisionGeometry.width, height = props.collisionGeometry.height,
        fill = "yellow"
      )
    )
  }
}
