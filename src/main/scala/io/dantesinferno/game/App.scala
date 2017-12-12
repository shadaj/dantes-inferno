package io.dantesinferno.game

import me.shadaj.slinky.core.Component
import me.shadaj.slinky.core.annotations.react
import me.shadaj.slinky.web.html._
import org.scalajs.dom

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

    val scaledWidth = if (dom.window.innerHeight < dom.window.innerWidth * (9D / 16)) {
      dom.window.innerHeight * (16D / 9)
    } else {
      dom.window.innerWidth
    }

    val scaledHeight = scaledWidth * (9D / 16)

    val scale = scaledHeight / 450

    div(style := js.Dynamic.literal(width = scaledWidth, height = scaledHeight, marginLeft = "auto", marginRight = "auto", display = "flex", alignItems = "center"))(
      Stage(width = scaledWidth, height = scaledHeight, scaleX = scale, scaleY = scale)(
        World(WorldState(
          List(
            BackdropState(
              x = 0, y = 0, width = 8000, height = 3000
            ),
            MinotaurState(
              x = 28 * 50,
              spawnDantePosition = 28 * 50 + 1250,
              maxRight = 3400,
              y = 1000
            )
          ) ++
            genStairs(0, 1000, List(
              50, 50, 50, 51, 53, 55, 57, 59, 61,
              40, 40, 40, 40, 40,
              30, 30, 30, 30, 30, 30, 31, 32, 33, 34, 35,
              21, 20,
              10 // 28 blocks wide
            ))
          ++ List(
            GroundState(
              y = 1000, x = 0, width = 3400
            )
          ) ++
            genStairs(3400, 500, List(
              0, 50,
              40, 40, 40, 40, 40,
              39, 38, 35, 30, 30, 29, 29, 28, 28, 27, 27, 26,
              10, 10, 10, 10, 10, 10, 9, 8, 7, 6, 5, 5, 5, 5, 6, 7, 7, 8, 8, 9,
              7, 6, 5, 4, 3, 2, 1, 0
            )) // 47 blocks wide
          ++ List(
            GroundState(
              y = 500, x = 3400, width = 4600
            ),
            ChironState(
              x = 7300,
              y = 500
            ),
            CentaurState(
              x = 5600,
              y = 500,
              spawnDantePosition = 6500,
              stopMovingDantePosition = 7000,
              relaxDantePosition = 7000
            ),
            CentaurState(
              x = 5700,
              y = 500,
              spawnDantePosition = 6500,
              stopMovingDantePosition = 7000,
              relaxDantePosition = 7000
            ),
            CentaurState(
              x = 5900,
              y = 500,
              spawnDantePosition = 6500,
              stopMovingDantePosition = 1000000000,
              relaxDantePosition = 7000
            )
          ) ++ List(
            WallState(
              left = -200,
              right = 0
            ),
            WallState(
              left = 8400,
              right = 8600
            ),
            GroundState(
              y = 0
            ),
            VirgilState(
              x = 0, y = 1500, xVel = 0
            ),
            DanteState(
              x = 0, y = 1500
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
            ),
            3600D -> List(
              ("MinotaurState", "ARGGG (something angry he cant get dante welp)", Some(3000D), Some(3000)),
              ("VirgilState", "Good work Dante! ajsfldkjsalkdfj something more", Some(3000D), None)
            ),
            4900D -> List(
              ("VirgilState", "Be warned Dante, up ahead are the centaurs", None, None),
              ("VirgilState", "Some more info about centaurs look at me being smart", None, None)
            ),
            6500D -> List(
              ("VirgilState", "Dante! Here come some centaurs!", Some(6200D), Some(3000)),
              ("CentaurState", "Hello Dante, you must go with us to see Chiron.", Some(5800D), Some(3000))
            ),
            7200D -> List(
              ("VirgilState", "Hello Chiron, Dante and I are looking to travel through this circle.", Some(6800D), Some(3000)),
              ("ChironState", "Hello Virgil and Dante.", Some(6800D), Some(3000))
            )
          ),
          windowX = -20,
          windowY = 1300
        ))
      )
    )
  }
}
