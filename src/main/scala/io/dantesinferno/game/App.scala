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
              x = 0, y = 0, width = 10000, height = 3000
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
              relaxDantePosition = 7000,
              talks = true
            )
          ) ++
            genStairs(8000, 0, List(
              50, 50, 50, 49, 48, 47, 46, 45, 44,
              40, 40, 40, 40, 40,
              30, 30, 30, 30, 30, 30, 29, 28, 27, 26, 25,
              21, 20,
              10, 9, 8, 7, 6, 5, 4, 3, 2, 1 // 37 blocks wide
            ))
          ++ List(
            WallState(
              left = -200,
              right = 0
            ),
            WallState(
              left = 10000,
              right = 10200
            ),
            GroundState(
              y = 0
            ),
            DanteState(
              x = 0, y = 1500
            ),
            VirgilState(
              x = 0, y = 1500, xVel = 0
            )
          ),
          triggeredQuotes = List(
            75D -> List(
              ("DanteState", "Canto XII", Some(0D), None),
              ("VirgilState", "Here we go!", Some(0D), None),
              ("VirgilState", "Watch your step!", Some(0D), None),
              ("DanteState", "Yes master", Some(0D), None)
            ),
            1500D -> List(
              ("VirgilState", "Dante, be vigilant, there is a Minotaur somewhere here.", None, None)
            ),
            2650D -> List(
              ("MinotaurState", "ROAR ROAR ROAR", Some(28 * 50 + 500D), Some(4000)),
              ("DanteState", "What's that?", Some(2300D), Some(2000)),
              ("VirgilState", "It's the minotaur!", Some(2300D), Some(4000)),
              ("VirgilState", "Some people think it's a man with a bull's head", Some(2300D), Some(4000)),
              ("VirgilState", "but it's actually a bull with a man's head!", Some(2300D), Some(4000)),
              ("DanteState", "He is \"[biting] himself in rage like one insane.\" (12)", Some(2300D), Some(5000)),
              ("VirgilState", "Stop quoting yourself and run!", Some(2300D), Some(4000)),
              ("VirgilState", "\"Go quickly, while he's raging.\" (22)", Some(2300D), Some(6000))
            ),
            3600D -> List(
              ("MinotaurState", "ROAR ROAR ROAR", Some(3000D), Some(2000)),
              ("VirgilState", "That was close.", Some(3000D), Some(2000))
            ),
            3700D -> List(
              ("VirgilState", "Last time I was down here, \"this rock had not yet slid.\" (30)", Some(3600D), Some(5000)),
              ("DanteState", "You're doing it too.", Some(3600D), Some(5000)),
              ("VirgilState", "No, I'm quoting you.", Some(3600D), Some(5000)),
              ("VirgilState", "Did you know that down there, the river of blood", Some(3600D), Some(5000)),
              ("VirgilState", "boils those \"Whose violence hurt others\" (42)?", Some(3600D), Some(5000)),
              ("DanteState", "I just ate", Some(3600D), Some(3000)),
              ("VirgilState", "Want some blood noodles with blood soup?", Some(3600D), Some(4000))
            ),
            6500D -> List(
              ("CentaurState", "Watcha doin mate", Some(5800D), Some(3000)),
              ("VirgilState", "Take us to Chiron", Some(6000D), Some(3000)),
              ("CentaurState", "ok", Some(5800D), Some(3000)),
              ("VirgilState", "That was Nessus he's dead now", Some(6000D), Some(3000)),
              ("DanteState", "Figured. When can I go home?", Some(6000D), Some(3000)),
              ("VirgilState", "Already? You've still got 88 Cantos to go!", Some(6000D), Some(3000))
            ),
            7200D -> List(
              ("VirgilState", "This is the great Chiron, another centaur.", Some(6800D), Some(3000)),
              ("VirgilState", "\"They circle the // The moat by thousands\" (66).", Some(6800D), Some(3000)),
              ("ChironState", "Hello Virgil and Dante.", Some(6800D), Some(3000)),
              ("DanteState", "Moat?", Some(6800D), Some(3000)),
              ("DanteState", "Did you mean... river of blood, perhaps?", Some(6800D), Some(3000)),
              ("ChironState", "Looky here, its one of those things", Some(6800D), Some(3000)),
              ("ChironState", "whose \"steps displace // Objects his body touches?", Some(6800D), Some(3000)),
              ("ChironState", "Feet of the dead // Are not accustomed to behave like that.\" (74)", Some(6800D), Some(3000)),
              ("DanteState", "I stepped in a pothole.", Some(6800D), Some(3000)),
              ("VirgilState", "\"He is indeed alive\" (78)", Some(6800D), Some(3000)),
              ("VirgilState", "And he's a wimp, so could you like", Some(6800D), Some(3000)),
              ("VirgilState", "loan me a centaur to carry him or something?", Some(6800D), Some(3000)),
              ("ChironState", "Sure, 5 Drachma", Some(6800D), Some(3000)),
              ("VirgilState", "I only carry Denarii", Some(6800D), Some(3000)),
              ("ChironState", "Exhange rate is 3:1", Some(6800D), Some(3000)),
              ("VirgilState", "... fine.", Some(6800D), Some(3000)),
              ("ChironState", "Nessus!", Some(6800D), Some(3000)),
              ("ChironState", "\"Go back and guide them\" (92)", Some(6800D), Some(3000)),
              ("ChironState", "and then come back in time for dinner!", Some(6800D), Some(3000)),
              ("CentaurState", "k", Some(6800D), Some(3000)),
              ("DanteState", "What do they even eat down here?", Some(6800D), Some(3000)),
              ("VirgilState", "Don't ask.", Some(6800D), Some(3000))
            ),
            9500D -> List(
              ("VirgilState", "Onto the next Canto!", None, Some(3000)),
              ("DanteState", "Created By:", None, Some(3000)),
              ("DanteState", "Jing-Chen Peng", None, Some(3000)),
              ("DanteState", "and Shadaj Laddad", None, Some(3000)),
              ("DanteState", "Art custom designed by Jing-Chen", None, Some(3000)),
              ("DanteState", "Game mechanics implemented by Shadaj", None, Some(3000)),
              ("DanteState", "Designed and coded in under two weeks", None, Some(3000)),
              ("DanteState", "With over 2,500 lines of code", None, Some(3000)),
              ("DanteState", "And dozens of individual animation frames", None, Some(3000)),
              ("DanteState", "Thanks for playing!", None, Some(3000))
            )
          ),
          windowX = -20,
          windowY = 1300
        ))
      )
    )
  }
}
