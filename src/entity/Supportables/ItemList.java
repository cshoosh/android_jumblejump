package Entity.Supportables;

public enum ItemList{
	Balloon,
	Shield,
	Switch,
	Coin;
	
	public String getCode(){
		switch (this) {
		case Balloon:
			return "BALLOON";
		case Coin:
			return "COIN";
		case Shield:
			return "SHIELD";
		case Switch:
			return "SWITCH";
			
		default:
			return "";
		}
	}
	
}