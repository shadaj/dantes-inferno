package io.dantesinferno.game

import me.shadaj.slinky.core.Component
import me.shadaj.slinky.core.annotations.react
import me.shadaj.slinky.core.facade.ReactElement
import org.scalajs.dom
import org.scalajs.dom.raw.KeyboardEvent

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("resources/app.css", JSImport.Default)
@js.native
object AppCSS extends js.Object

@JSImport("resources/logo.svg", JSImport.Default)
@js.native
object ReactLogo extends js.Object

trait CollisionGeometry[Self] {
  def markOnGround(onGround: Boolean): Self
  def collideWith(obj: CollisionGeometry[_]): Self
}

trait CollisionBox[Self <: CollidingObjectState[Self]] extends CollisionGeometry[Self] {
  def left: Double
  def bottom: Double
  def right: Double
  def top: Double

  def state: Self
  def transform(newLeft: Double, newBottom: Double): Self

  override def collideWith(obj: CollisionGeometry[_]): Self = {
    obj match {
      case otherBox: CollisionBox[_] =>
        var afterCollision = state
        def myGeometry = afterCollision.collisionGeometry.asInstanceOf[CollisionBox[Self]]

        def intersectsX = myGeometry.left < otherBox.right && myGeometry.right > otherBox.left
        def intersectsY = myGeometry.bottom < otherBox.top && myGeometry.top > otherBox.bottom

        if ((myGeometry.left + 5 < otherBox.right && myGeometry.right - 5 > otherBox.left) && intersectsY && myGeometry.bottom > otherBox.bottom) {
          afterCollision = transform(myGeometry.left, otherBox.top)
          afterCollision = myGeometry.markOnGround(true)
        }

        if ((myGeometry.left + 5 < otherBox.right && myGeometry.right - 5 > otherBox.left) && myGeometry.bottom <= otherBox.top && myGeometry.top > otherBox.bottom) {
          afterCollision = myGeometry.markOnGround(true)
        }

        if (intersectsX && intersectsY && myGeometry.top < otherBox.top) {
          afterCollision = transform(myGeometry.left, otherBox.bottom - (myGeometry.top - myGeometry.bottom))
        }

        if (intersectsX && intersectsY && myGeometry.left < otherBox.left) {
          afterCollision = transform(otherBox.left - (myGeometry.right - myGeometry.left), myGeometry.bottom)
        }

        if (intersectsX && intersectsY && myGeometry.right > otherBox.right) {
          afterCollision = transform(otherBox.right, myGeometry.bottom)
        }

        if (myGeometry.bottom <= 0) {
          afterCollision = transform(myGeometry.left, 0)
          afterCollision = myGeometry.markOnGround(true)
        }

        afterCollision
    }
  }
}

trait CollidingObjectState[Self <: CollidingObjectState[Self]] extends ObjectState[Self] { self: Self =>
  val collisionGeometry: CollisionGeometry[Self]

  override def update(worldState: WorldState): Self = {
    worldState.objects.filterNot(_ == this).filter(_.isInstanceOf[CollidingObjectState[_]]).foldLeft(collisionGeometry.markOnGround(false)) { (self, obj) =>
      self.collisionGeometry.collideWith(obj.asInstanceOf[CollidingObjectState[_]].collisionGeometry)
    }
  }
}

trait ObjectState[Self <: ObjectState[Self]] {
  def update(worldState: WorldState): Self
  def render: ReactElement
}

case class WorldState(objects: List[ObjectState[_]])

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

@react class App extends Component {
  type Props = Unit

  type State = WorldState

  override def initialState: App.State = {
    WorldState(
      List(
        DanteState(
          0, 0, 0, 0, 0, true
        ),
        DanteState(
          75, 0, 0, 0, 0, true
        )
      )
    )
  }

  private val css = AppCSS

  def onKeyDown(key: KeyboardEvent): Unit = {
    key.key match {
      case "ArrowRight" => setState(state.copy(objects = state.objects.head.asInstanceOf[DanteState].copy(xAcc = 2) :: state.objects.tail))
      case "ArrowLeft" => setState(state.copy(objects = state.objects.head.asInstanceOf[DanteState].copy(xAcc = -2) :: state.objects.tail))
      case "ArrowUp" =>
        if (state.objects.head.asInstanceOf[DanteState].onGround) { // TODO: fix this
          setState(state.copy(objects = state.objects.head.asInstanceOf[DanteState].copy(yVel = 20) :: state.objects.tail))
        }

      case o => println(s"Unhandled key! $o")
    }
  }

  def onKeyUp(key: KeyboardEvent): Unit = {
    key.key match {
      case "ArrowRight" => setState(state.copy(objects = state.objects.head.asInstanceOf[DanteState].copy(xAcc = 0) :: state.objects.tail))
      case "ArrowLeft" => setState(state.copy(objects = state.objects.head.asInstanceOf[DanteState].copy(xAcc = 0) :: state.objects.tail))
      case "ArrowUp" =>
      case o => println(s"Unhandled key! $o")
    }
  }

  def animateFrame(): Unit = {
//    setState(state.copy(
//      danteState = state.danteState.update(???),
//      windowX = if (state.danteState.x > state.windowX + (800 - 10 - 50)) {
//        state.danteState.x - (800 - 10 - 50)
//      } else if (state.danteState.x < state.windowX + 10) {
//        state.danteState.x - 10
//      } else {
//        state.windowX
//      }
//    ))

    setState(state.copy(objects = state.objects.map(_.update(state).asInstanceOf[ObjectState[_]])))

    dom.window.requestAnimationFrame(something => {
      animateFrame()
    })
  }

  override def componentDidMount(): Unit = {
    dom.document.onkeydown = key => {
      onKeyDown(key)
    }

    dom.document.onkeyup = key => {
      onKeyUp(key)
    }

    animateFrame()
  }

  override def shouldComponentUpdate(nextProps: Unit, nextState: State): Boolean = {
    props != nextProps || state != nextState
  }

  override def componentDidUpdate(prevProps: Props, prevState: State): Unit = {
    dom.document.onkeydown = key => {
      onKeyDown(key)
    }

    dom.document.onkeyup = key => {
      onKeyUp(key)
    }
  }

  def render() = {
    Stage(width = 800, height = 450)(
      Layer(x = /*-state.windowX*/ 0D)(
        state.objects.map(_.render)
      )
    )
  }
}
