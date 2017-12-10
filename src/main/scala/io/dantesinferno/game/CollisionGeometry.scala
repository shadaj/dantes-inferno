package io.dantesinferno.game

trait CollisionGeometry[Self] {
  def markOnGround(onGround: Boolean): Self
  def collideWith(obj: CollisionGeometry[_]): Self
}
