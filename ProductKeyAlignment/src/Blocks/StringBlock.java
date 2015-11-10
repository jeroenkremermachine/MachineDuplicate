package Blocks;

public class StringBlock implements Block{

	String st;
	String type;
	
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
	
}