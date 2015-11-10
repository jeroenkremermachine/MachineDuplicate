package KeyTypes;

public class Double1Type implements Type {
	
	String key = new String();
	
	public Double1Type(String inKey){
		key = inKey;
	}
	
	public String getType(){
		return "Double1";
	}
	
	public String getKey(){
		return key;
	}
}
