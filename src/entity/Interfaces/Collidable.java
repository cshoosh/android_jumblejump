package Entity.Interfaces;

import EntityClasses.Entity;

public interface Collidable{
	public abstract void CollideTop(Entity who);
	public abstract void CollideInside(Entity who);
	public abstract void CollideBottom(Entity who);
}