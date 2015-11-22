package Blocks;

public class StringBlock implements Block{

	String st;
	String type;
	String subType = "leeg";
	
	public StringBlock(String s){
		st = s;
		type = "String";
	}
	
	
	public String getBlock(){
		return st;
	}
	
	public String getType(){
		return type;
	}
	
	public String getSubType(){
		return subType;
	}
	
}