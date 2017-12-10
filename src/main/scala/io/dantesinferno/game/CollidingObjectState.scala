package io.dantesinferno.game

trait CollidingObjectState[Self <: CollidingObjectState[Self]] extends ObjectState[Self] { self: Self =>
  val collisionGeometry: CollisionGeometry[Self]

  override def update(worldState: WorldState): Self = {
    worldState.objects.filterNot(_ == this).filter(_.isInstanceOf[CollidingObjectState[_]]).foldLeft(collisionGeometry.markOnGround(false)) { (self, obj) =>
      self.collisionGeometry.collideWith(obj.asInstanceOf[CollidingObjectState[_]].collisionGeometry)
    }
  }
}
