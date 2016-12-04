package Resources;

import java.util.HashMap;
import java.util.Map;

import Entity.Supportables.DrawableObject;

public class ResourceObject {
	public ResourceObject(ResourceName name){
		mName = name;
		mObject = new HashMap<String, DrawableObject>();
	}
		
	private Map<String, DrawableObject> mObject;
	private ResourceName mName;

	
	public DrawableObject getObject(String sub) {
		return (DrawableObject) mObject.get(sub);
	}
		
	public void addObject(String sub, DrawableObject object){
		mObject.put(sub, object);
	}

	public ResourceName getName() {
		return mName;
	}
	
	public void replaceObject(String sub, DrawableObject object){
		mObject.remove(sub);
		mObject.put(sub, object);
	}
}

