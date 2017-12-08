package io.dantesinferno.game

import me.shadaj.slinky.core.{ExternalComponent, ExternalComponentNoProps}
import me.shadaj.slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("react-konva", JSImport.Default)
@js.native
object ReactKonva extends js.Object {
  val Layer: js.Object = js.native
  val Group: js.Object = js.native
  val Rect: js.Object = js.native
  val Stage: js.Object = js.native
}

@react object Stage extends ExternalComponent {
  case class Props(width: Int, height: Int)

  override val component: |[String, js.Object] = ReactKonva.Stage
}

object Layer extends ExternalComponentNoProps {
  override val component: |[String, js.Object] = ReactKonva.Layer
}

@react object Group extends ExternalComponent {
  case class Props(x: js.UndefOr[Double] = js.undefined,
                   y: js.UndefOr[Double] = js.undefined)

  override val component: |[String, js.Object] = ReactKonva.Group
}

@react object Rect extends ExternalComponent {
  case class Props(x: Int, y: Int,
                   width: Int, height: Int,
                   fill: js.UndefOr[String] = js.undefined)

  override val component: |[String, js.Object] = ReactKonva.Rect
}
