package Blocks;

public class UnitMeasureBlock implements Block {

		String st;
		String type;
		String subType = "leeg";
		
		public UnitMeasureBlock(String s){
			st = s;
			type = "Unit Measure";
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
