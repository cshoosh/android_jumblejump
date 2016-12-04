package EntityClasses;

import Entity.Interfaces.Collidable;
import Entity.Supportables.EntityInfo;
import Process.AllManagers;


public abstract class Platform extends Entity implements Collidable {

	public Platform(EntityInfo info, AllManagers managers) {
		super(managers); 
	}
	
	
	
}