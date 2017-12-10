package io.dantesinferno.game

import me.shadaj.slinky.core.facade.ReactElement

trait ObjectState[Self <: ObjectState[Self]] {
  def update(worldState: WorldState): Self
  def render: ReactElement
}
