package Entity.Enemies;

import Entity.Supportables.EntityInfo;
import EntityClasses.Enemy;
import Process.AllManagers;


public class Boss extends Enemy {

	public Boss(EntityInfo info, AllManagers context) {
		super(info, context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void score() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void die() {
		super.die();
		mManagers.getEntityManager().getPlayer().bigboss();
	}

}
