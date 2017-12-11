package io.dantesinferno.game

trait CollisionBox[Self <: CollidingObjectState[Self]] extends CollisionGeometry[Self] {
  def left: Double
  def bottom: Double
  def right: Double
  def top: Double

  def width: Double = right - left
  def height: Double = top - bottom

  def state: Self
  def transform(newLeft: Double, newBottom: Double): Self

  override def collideWith(obj: CollisionGeometry[_]): Self = {
    obj match {
      case otherBox: CollisionBox[_] =>
        var afterCollision = state
        def myGeometry = afterCollision.collisionGeometry.asInstanceOf[CollisionBox[Self]]

        def significantlyIntersectsX = myGeometry.left + 5 < otherBox.right && myGeometry.right - 5 > otherBox.left
        def intersectsX = myGeometry.left < otherBox.right && myGeometry.right > otherBox.left
        def intersectsY = myGeometry.bottom < otherBox.top && myGeometry.top > otherBox.bottom

        if (significantlyIntersectsX && intersectsY && myGeometry.bottom > otherBox.bottom) {
          afterCollision = transform(myGeometry.left, otherBox.top)
          afterCollision = myGeometry.markOnGround(true)
        }

        if (significantlyIntersectsX && myGeometry.bottom <= otherBox.top && myGeometry.top > otherBox.bottom) {
          afterCollision = myGeometry.markOnGround(true)
        }

        if (significantlyIntersectsX && intersectsY && myGeometry.top < otherBox.top) {
          afterCollision = transform(myGeometry.left, otherBox.bottom - myGeometry.height)
        }

        if (intersectsX && intersectsY && myGeometry.left < otherBox.left) {
          afterCollision = transform(otherBox.left - myGeometry.width, myGeometry.bottom)
        }

        if (intersectsX && intersectsY && myGeometry.right > otherBox.right) {
          afterCollision = transform(otherBox.right, myGeometry.bottom)
        }

        afterCollision
    }
  }
}
