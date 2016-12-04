package Entity.Enemies;

import Entity.Supportables.EntityInfo;
import EntityClasses.Enemy;
import Process.AllManagers;
import Resources.ResourceEnemy;
import Resources.ResourceName;


public class Wazir extends Enemy {

	public Wazir(EntityInfo info, AllManagers context) {
		super(info, context);
	}

	@Override
	protected void init() {
		addDrawable(ResourceName.Enemy, ResourceEnemy.Croc.name());
	}

	@Override
	protected void score() {
	}

}
