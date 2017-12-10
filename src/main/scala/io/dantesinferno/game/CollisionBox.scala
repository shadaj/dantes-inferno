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

        def intersectsX = myGeometry.left < otherBox.right && myGeometry.right > otherBox.left
        def intersectsY = myGeometry.bottom < otherBox.top && myGeometry.top > otherBox.bottom

        if ((myGeometry.left + 5 < otherBox.right && myGeometry.right - 5 > otherBox.left) && intersectsY && myGeometry.bottom > otherBox.bottom) {
          afterCollision = transform(myGeometry.left, otherBox.top)
          afterCollision = myGeometry.markOnGround(true)
        }

        if ((myGeometry.left + 5 < otherBox.right && myGeometry.right - 5 > otherBox.left) && myGeometry.bottom <= otherBox.top && myGeometry.top > otherBox.bottom) {
          afterCollision = myGeometry.markOnGround(true)
        }

        if (intersectsX && intersectsY && myGeometry.top < otherBox.top) {
          afterCollision = transform(myGeometry.left, otherBox.bottom - (myGeometry.top - myGeometry.bottom))
        }

        if (intersectsX && intersectsY && myGeometry.left < otherBox.left) {
          afterCollision = transform(otherBox.left - (myGeometry.right - myGeometry.left), myGeometry.bottom)
        }

        if (intersectsX && intersectsY && myGeometry.right > otherBox.right) {
          afterCollision = transform(otherBox.right, myGeometry.bottom)
        }

        if (myGeometry.bottom <= 0) {
          afterCollision = transform(myGeometry.left, 0)
          afterCollision = myGeometry.markOnGround(true)
        }

        afterCollision
    }
  }
}
