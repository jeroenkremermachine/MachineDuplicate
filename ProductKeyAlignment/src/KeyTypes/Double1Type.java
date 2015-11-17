package KeyTypes;

public class Double1Type implements Type {
	
	String key = new String();
	String UnitMeasure = new String();
	
	public Double1Type(String inKey){
		key = inKey;
	}
	
	public String getType(){
		return "Double1";
	}
	
	public String getKey(){
		return key;
	}
	
	public void setUnitMeasure(String UM){
		UnitMeasure = UM;
	}
	
	public String getUnitMeasure(){
		return UnitMeasure;
	}
}
