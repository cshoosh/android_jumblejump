package Entity.Enemies;

import Entity.Supportables.EntityInfo;
import EntityClasses.Enemy;
import Process.AllManagers;
import Resources.ResourceEnemy;
import Resources.ResourceName;


public class Pawn extends Enemy {


	public Pawn(EntityInfo info, AllManagers context) {
		super(info, context);
	}

	@Override
	protected void init() {
		addDrawable(ResourceName.Enemy, ResourceEnemy.Spider.name());
	}

	@Override
	protected void score() {
		mManagers.getEntityManager().getPlayer().increaseScore(50);
	}

}
