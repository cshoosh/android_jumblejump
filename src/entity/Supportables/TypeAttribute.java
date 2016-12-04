package Entity.Supportables;



public class TypeAttribute {
	private String mValue;
	private TypeAttributeList mName;
	
	public TypeAttribute(TypeAttributeList Name, String Value) {
		this.mName = Name;
		this.mValue = Value;
	}

	public TypeAttributeList getName() {
		return mName;
	}

	public String getValue() {
		return mValue;
	}
}
