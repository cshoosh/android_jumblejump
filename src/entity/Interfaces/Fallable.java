package Entity.Interfaces;

public interface Fallable{
	public abstract boolean isFalling();
	public abstract void setFalling(boolean falling);
	
	public static final String FALL_KEY = "entityFallKey";
}