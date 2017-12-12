package io.dantesinferno.game

import me.shadaj.slinky.core.{ExternalComponent, ExternalComponentNoProps}
import me.shadaj.slinky.core.annotations.react
import org.scalajs.dom.html.Image

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
  val Image: js.Object = js.native
  val Text: js.Object = js.native
}

@react object Stage extends ExternalComponent {
  case class Props(width: Double, height: Double, scaleX: Double, scaleY: Double)

  override val component: |[String, js.Object] = ReactKonva.Stage
}

@react object Layer extends ExternalComponent {
  case class Props(x: js.UndefOr[Double] = js.undefined,
                   y: js.UndefOr[Double] = js.undefined)

  override val component: |[String, js.Object] = ReactKonva.Layer
}

@react object Group extends ExternalComponent {
  case class Props(x: js.UndefOr[Double] = js.undefined,
                   y: js.UndefOr[Double] = js.undefined)

  override val component: |[String, js.Object] = ReactKonva.Group
}

@react object Rect extends ExternalComponent {
  case class Props(x: Double, y: Double,
                   width: Double, height: Double,
                   fill: js.UndefOr[String] = js.undefined,
                   fillPatternImage: js.UndefOr[Image] = js.undefined,
                   fillPatternRepeat: js.UndefOr[String] = js.undefined)

  override val component: |[String, js.Object] = ReactKonva.Rect
}

case class Crop(x: Double, y: Double, width: Double, height: Double)

@react object Image extends ExternalComponent {
  case class Props(image: Image,
                   x: Double, y: Double,
                   width: Double, height: Double,
                   crop: Option[Crop] = None)

  override val component: |[String, js.Object] = ReactKonva.Image
}

@react object Text extends ExternalComponent {
  case class Props(x: Double, y: Double, width: Double,
                   text: String,
                   fontSize: Int,
                   fontFamily: String,
                   align: js.UndefOr[String] = js.undefined,
                   fill: js.UndefOr[String] = js.undefined)

  override val component: |[String, js.Object] = ReactKonva.Text
}
