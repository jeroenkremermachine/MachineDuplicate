package KeyTypes;

public class StringType implements Type {
	
	String key = new String();
	String subType = new String();
	
	public StringType(String inKey, String inSubType){
		key = inKey;
		subType = inSubType;
	}
	
	public String getType(){
		return "String";
	}
	
	public String getSubType(){
		return subType;
	}
	
	public String getKey(){
		return key;
	}
}
