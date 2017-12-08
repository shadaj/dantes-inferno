package io.dantesinferno.game

import me.shadaj.slinky.core.Component
import me.shadaj.slinky.core.annotations.react
import me.shadaj.slinky.core.facade.ReactElement

@react class Dante extends Component {
  type Props = DanteState

  override def render(): ReactElement = {
    Group(x = props.x, y = (450 - props.y) - 100)(
      Rect(
        x = 0, y = 0,
        width = 50, height = 100,
        fill = "yellow"
      )
    )
  }
}
