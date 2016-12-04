package Entity.Enemies;

import Entity.Supportables.EntityInfo;
import EntityClasses.Enemy;
import Process.AllManagers;
import Resources.ResourceEnemy;
import Resources.ResourceName;


public class Knight extends Enemy {

	
	//private static final int IDLE = 0;
	private static final int DOWN = 1;
	
	public Knight(EntityInfo info, AllManagers context) {
		super(info, context);
	}

	@Override
	protected void init() {
		addDrawable(ResourceName.Enemy, ResourceEnemy.Snake.name());
		addDrawable(ResourceName.Enemy, ResourceEnemy.SnakeHit.name());
	}
	
	@Override
	public void update() {
		super.update();
		
		if (isFalling())
			setLevel(DOWN);
	}

	@Override
	protected void score() {
		mManagers.getEntityManager().getPlayer().increaseScore(150);
	}

}
