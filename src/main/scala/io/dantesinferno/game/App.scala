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
              y = 500, x = 3400, width = 4800
            ),
            ChironState(
              x = 7300,
              y = 500
            ),
            CentaurState(
              x = 5500,
              y = 500,
              spawnDantePosition = 6800,
              stopMovingDantePosition = 7000,
              relaxDantePosition = 7000
            ),
            CentaurState(
              x = 5800,
              y = 500,
              spawnDantePosition = 6800,
              stopMovingDantePosition = 7000,
              relaxDantePosition = 7000
            ),
            CentaurState(
              x = 6100,
              y = 500,
              spawnDantePosition = 6800,
              stopMovingDantePosition = 1000000000,
              relaxDantePosition = 7000,
              talks = true
            )
          ) ++
            genStairs(8200, 0, List(
              48, 47, 46, 45, 44,
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
            ),
            RiverOfBloodBackdropState(
              x = 7560, y = 260
            )
          ),
          triggeredQuotes = List(
            75D -> List(
              ("DanteState", "Canto XII", Some(0D), Some(3000)),
              ("VirgilState", "Here we go!", Some(0D), Some(2000)),
              ("VirgilState", "Watch your step!", Some(0D), Some(2000)),
              ("DanteState", "Yes master", Some(0D), Some(1000))
            ),
            1500D -> List(
              ("VirgilState", "Dante, be vigilant, there is a Minotaur somewhere here.", None, Some(3000))
            ),
            2650D -> List(
              ("MinotaurState", "ROAR ROAR ROAR", Some(28 * 50 + 500D), Some(3000)),
              ("DanteState", "What's that?", None, Some(2000)),
              ("VirgilState", "It's the minotaur!", None, Some(2000)),
              ("VirgilState", "Some people think it's a man with a bull's head", None, Some(3000)),
              ("VirgilState", "but it's actually a bull with a man's head!", None, Some(3000)),
              ("DanteState", "He is \"[biting] himself in rage like one insane.\" (12)", None, Some(3000)),
              ("VirgilState", "Stop quoting yourself and run!", None, Some(2000)),
              ("VirgilState", "\"Go quickly, while he's raging.\" (22)", None, Some(3000))
            ),
            3600D -> List(
              ("MinotaurState", "ROAR ROAR ROAR", Some(3000D), Some(2000)),
              ("VirgilState", "That was close.", None, Some(1000))
            ),
            3700D -> List(
              ("VirgilState", "Last time I was down here, \"this rock had not yet slid.\" (30)", None, Some(3000)),
              ("DanteState", "You're doing it too.", None, Some(1000)),
              ("VirgilState", "No, I'm quoting you.", None, Some(1000)),
              ("VirgilState", "Did you know that down there, the river of blood", None, Some(3000)),
              ("VirgilState", "boils those \"Whose violence hurt others\" (42)?", None, Some(3000)),
              ("DanteState", "I just ate", None, Some(1000)),
              ("VirgilState", "Want some blood noodles with blood soup?", None, Some(2000))
            ),
            6800D -> List(
              ("CentaurState", "Watcha doin mate", Some(6000D), Some(3000)),
              ("VirgilState", "Take us to Chiron", None, Some(3000)),
              ("CentaurState", "ok", None, Some(1000)),
              ("VirgilState", "That was Nessus he's dead now", None, Some(3000)),
              ("DanteState", "Figured. When can I go home?", None, Some(3000)),
              ("VirgilState", "Already? You've still got 88 Cantos to go!", None, Some(3000))
            ),
            7200D -> List(
              ("VirgilState", "This is the great Chiron, another centaur.", None, Some(3000)),
              ("VirgilState", "\"They circle the // The moat by thousands\" (66).", None, Some(3000)),
//              ("ChironState", "Hello Virgil and Dante.", Some(6800D), Some(3000)),
              ("DanteState", "Moat?", None, Some(1000)),
              ("DanteState", "Did you mean... river of blood, perhaps?", None, Some(2000)),
              ("ChironState", "Looky here, its one of those things", None, Some(3000)),
              ("ChironState", "whose \"steps displace // Objects his body touches?", None, Some(3000)),
              ("ChironState", "Feet of the dead // Are not accustomed to behave like that.\" (74)", None, Some(3000)),
              ("DanteState", "I stepped in a pothole.", None, Some(3000)),
              ("VirgilState", "\"He is indeed alive\" (78)", None, Some(3000)),
              ("VirgilState", "And he's a wimp, so could you like", None, Some(3000)),
              ("VirgilState", "loan me a centaur to carry him or something?", None, Some(3000)),
              ("ChironState", "Sure, 5 Drachma", None, Some(2000)),
              ("VirgilState", "I only carry Denarii", None, Some(2000)),
              ("ChironState", "Exhange rate is 3:1", None, Some(2000)),
              ("VirgilState", "... fine.", None, Some(1000)),
              ("ChironState", "Nessus!", None, Some(1000)),
              ("ChironState", "\"Go back and guide them\" (92)", None, Some(2000)),
              ("ChironState", "and then come back in time for dinner!", None, Some(2000)),
              ("CentaurState", "k", None, Some(1000)),
              ("DanteState", "What do they even eat down here?", None, Some(2000)),
              ("VirgilState", "Don't ask.", None, Some(2000))
            ),
            9500D -> List(
              ("VirgilState", "Onto the next Canto!", None, Some(3000)),
              ("DanteState", "Created By:", None, Some(3000)),
              ("DanteState", "Jing-Chen Peng", None, Some(3000)),
              ("DanteState", "and Shadaj Laddad", None, Some(3000)),
              ("DanteState", "Art custom designed by Jing-Chen", None, Some(3000)),
              ("DanteState", "Game physics and mechanics implemented by Shadaj", None, Some(3000)),
              ("DanteState", "Designed and coded from scratch in under one week", None, Some(3000)),
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
