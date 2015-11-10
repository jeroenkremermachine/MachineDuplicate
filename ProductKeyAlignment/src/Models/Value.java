package Models;


public class Value
{
	
	private String value;
	private String[] blockList;
	
	public Value(String v)
	{
		value = v;
		blockList = v.split("[^A-Za-z0-9,.:/-]+|(?<=[A-Za-z])(?=[0-9])|(?<=[0-9])(?=[A-Za-z])");
		
	}

	public int getNrBlocks(){
		return blockList.length;
	}
	
	public String getBlock(int k){
		return blockList[k];
	}
	
	public String[] getBlockList(){
		return blockList;
	}
	
	public String getString()
	{
		return value;
	}
		

}
