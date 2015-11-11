package Blocks;

public class BlockRecognizer {
	
	public BlockRecognizer(){
	
	}
	
	public Block createBlock(String s){

		if(this.recognizeDouble(s)){
			return new DoubleBlock(s);
		}
		else if(this.recognizeUnitMeasure(s)){
			return new UnitMeasureBlock(s);
		}
		else {
			return new StringBlock(s);
		}
		
	}
	
	public boolean recognizeDouble(String s){
		return (s.matches("[0-9,./:-]+") && !s.matches("[,./:-]+"));
	}
	
	public boolean recognizeUnitMeasure(String s){
		
		//we check the following measures:
//		Kilogram &Kg\\
//	    Hertz & Hz\\
//	    Watt &	W \\
//	    Volt & V\\
//	    Bel, Decibel & B, DB\\
//	    Inch & " \\
//	    Joule & J\\
		if(s.matches("Kilogram")){
			return true;
		}
		else if(s.matches("Kg")){
			return true;
		}
		else if(s.matches("Lbs")){
			return true;
		}
		else if(s.matches("lbs")){
			return true;
		}
		else if(s.matches("Hertz")){
			return true;
		}
		else if(s.matches("Hz")){
			return true;
		}
		else if(s.matches("Watt")){
			return true;
		}
		else if(s.matches("W")){
			return true;
		}
		else if(s.matches("Volt")){
			return true;
		}
		else if(s.matches("V")){
			return true;
		}
		else if(s.matches("Bel")){
			return true;
		}
		else if(s.matches("Decibel")){
			return true;
		}
		else if(s.matches("B")){
			return true;
		}
		else if(s.matches("DB")){
			return true;
		}
		else if(s.matches("Inch")){
			return true;
		}
		else if(s.matches("\"")){
			return true;
		}
		else if(s.matches("Joule")){
			return true;
		}
		else if(s.matches("J")){
			return true;
		}
		else if(s.matches("lb")){
			return true;
		}
		else{
			return false;
		}

		
	}
	
	
}
