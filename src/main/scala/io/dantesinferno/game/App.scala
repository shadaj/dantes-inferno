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
    def genStairs(left: Int, baseY: Int, heights: List[Int]): List[ObjectState[_]] = {
      heights.zipWithIndex.map { case (height, index) =>
        StaticBoxState(
          x = left + index * 50, y = baseY,
          width = 50, height = height * 10
        )
      }
    }

    div(style := js.Dynamic.literal(width = "800px", height = "100vh", marginLeft = "auto", marginRight = "auto", display = "flex", alignItems = "center"))(
      Stage(width = 800, height = 450)(
        World(WorldState(
          List(
            MinotaurState(
              x = 28 * 50,
              spawnDantePosition = 28 * 50 + 1250,
              y = 500
            )
          ) ++
            genStairs(0, 500, List(
              50, 50, 50, 51, 53, 55, 57, 59, 61,
              40, 40, 40, 40, 40,
              30, 30, 30, 30, 30, 30, 31, 32, 33, 34, 35,
              21, 20,
              10 // 28 blocks wide
            ))
          ++ List(
            GroundState(
              y = 500, x = 0, width = 28 * 50 + 2000
            ),
            WallState(
              left = -200,
              right = 0
            ),
            WallState(
              left = 3400,
              right = 3600
            ),
            GroundState(
              y = 0
            ),
            VirgilState(
              x = 0, y = 1000, xVel = 0
            ),
            DanteState(
              x = 0, y = 1000
            )
          ),
          triggeredQuotes = List(
            75D -> List(
              ("VirgilState" ,"This is the entrance to the amaze amaze canto amaze canto amaze", Some(0D), None),
              ("DanteState", "Hello I am dante lolz", Some(0D), None)
            ),
            1500D -> List(
              ("VirgilState", "WOW I AM INTRODUCING THE MINOTAUR", None, None)
            ),
            2650D -> List(
              ("MinotaurState", "HADLFJLAKSDJFKLADSJF", Some(28 * 50 + 500D), Some(4000)),
              ("VirgilState", "Run Dante! For here comes the Minotaur!", None, Some(3000))
            )
          ),
          windowX = -20,
          windowY = 800
        ))
      )
    )
  }
}
