package Blocks;

public class DoubleBlock implements Block{

	String st;
	String type;
	
	public DoubleBlock(String s){
		st = s;
		type = "Double";
	}
	
	
	public String getBlock(){
		return st;
	}
	
	public String getType(){
		return type;
	}
	
}