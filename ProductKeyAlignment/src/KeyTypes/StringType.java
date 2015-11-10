package KeyTypes;

public class StringType implements Type {
	
	String key = new String();
	
	public StringType(String inKey){
		key = inKey;
	}
	
	public String getType(){
		return "String";
	}
	
	public String getKey(){
		return key;
	}
}
