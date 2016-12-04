package Resources;

public class Animation {
	public static final float tendToZero(float value, float amount){
		float ret = value;
		if (value > 0)
			ret -= amount;
		else if (value < 0)
			ret += amount;
		else
			return 0;
		
		if (ret > 0 && value < 0)
			return 0;
		else if (ret < 0 && value > 0)
			return 0;
		
		return ret;
	}
	
	public static final float tendToValue (float pass, float amount, float value){
		float ret = pass;
		if (pass > value)
			ret -= amount;
		else if (pass < value)
			ret += amount;
		else
			return value;
		
		if (ret > value && pass < value)
			return value;
		else if (ret < value && pass > value)
			return value;
		
		return ret;
	}
}
