package Entity.Supportables;


import Entity.Enemies.Boss;
import Entity.Enemies.Knight;
import Entity.Enemies.Pawn;
import Entity.Enemies.Wazir;
import EntityClasses.Enemy;
import Process.AllManagers;


public enum EnemyList{
	Pawn,
	Knight,
	Wazir,
	Boss;
	
	public static final EnemyList translate (String value){
		if (value.equals("PWN"))
			return Pawn;
		else if(value.equals("KNT"))
			return Knight;
		else if (value.equals("WZR"))
			return Wazir;
		else if (value.equals("BSS"))
			return Boss;
		
		return null;
	}
	
	public static final Enemy getEnemyIstance (EntityInfo info, AllManagers context){
		EnemyList list = translate(info.getAttributeValue(TypeAttributeList.sub));
		
		switch (list) {
		case Pawn:
			return new Pawn(info, context);
		case Knight:
			return new Knight(info, context);
		case Wazir:
			return new Wazir(info, context);
		case Boss:
			return new Boss(info, context);
		default:
			break;
		}
		
		return null;
	}
}