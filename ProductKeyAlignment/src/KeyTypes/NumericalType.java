package KeyTypes;

public class NumericalType implements Type {
	
	String key = new String();
	String subType = new String();
	String UnitMeasure = new String();
	
	public NumericalType(String inKey, String inSubType){
		key = inKey;
		subType = inSubType;
	}
	
	public String getType(){
		return "Numerical";
	}
	
	public String getKey(){
		return key;
	}
	
	public String getSubType(){
		return subType;
	}
	
	public void setUnitMeasure(String UM){
		UnitMeasure = UM;
	}
	
	public String getUnitMeasure(){
		return UnitMeasure;
	}
}
