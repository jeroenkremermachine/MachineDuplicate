package Blocks;

public class NumericalBlock implements Block{

	String st;
	String type;
	String subtype;
	
	public NumericalBlock(String s, String sts){
		st = s;
		type = "Numerical";
		subtype = sts;
	}
	
	
	public String getBlock(){
		return st;
	}
	
	public String getType(){
		return type;
	}
	
	public String getSubType(){
		return subtype;
	}
}