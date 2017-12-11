package io.dantesinferno.game

import me.shadaj.slinky.core.Component
import me.shadaj.slinky.core.annotations.react
import me.shadaj.slinky.web.html._

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("resources/app.css", JSImport.Default)
@js.native
object AppCSS extends js.Object

@react class App extends Component {
  type Props = Unit

  private val css = AppCSS

  def render() = {
    div(style := js.Dynamic.literal(width = "800px", height = "100vh", marginLeft = "auto", marginRight = "auto", display = "flex", alignItems = "center"))(
      Stage(width = 800, height = 450)(
        World(WorldState(
          List(
            StaticBoxState(
              x = 150, y = 50
            ),
            WallState(
              x = -50
            ),
            WallState(
              x = 1000
            ),
            GroundState(
              y = 0
            ),
            VirgilState(
              x = 0, y = 50, xVel = 0
            ),
            DanteState(
              x = 75, y = 50
            )
          ),
          queuedQuotes = List(
            classOf[VirgilState] -> "This is the entrance to the amaze amaze canto amaze canto amaze",
            classOf[DanteState] -> "Hello I am dante lolz"
          ),
          windowX = -20
        ))
      )
    )
  }
}
