package LevelDesign;

import java.util.ArrayList;

import Entity.Supportables.EntityInfo;

public class StageParser{
	private ArrayList<EntityInfo> mInfo;
	
	public StageParser() {
		mInfo = new ArrayList<EntityInfo>();
	}
	
	public void addInfo(EntityInfo info){
		mInfo.add(info);
	}
	
	public EntityInfo getInfo(int index) throws ArrayIndexOutOfBoundsException{
		return mInfo.get(index);
	}
	
	public int getEntityCount(){
		return mInfo.size();
	}
	
	public void clear(){
		mInfo.clear();
	}
}