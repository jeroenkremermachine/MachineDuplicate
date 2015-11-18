package Blocks;

public class BlockRecognizer {
	
	public BlockRecognizer(){
	
	}
	
	public Block createBlock(String s){
		String UnitMeasure = this.recognizeUnitMeasure(s);
		
		if(this.recognizeDouble(s)){
			return new DoubleBlock(s);
		}
		else if(!UnitMeasure.equals("leeg")){
			
			return new UnitMeasureBlock(UnitMeasure);
		}
		else {
			return new StringBlock(s);
		}
		
	}
	
	public boolean recognizeDouble(String s){
		return (s.matches("[0-9,./:-]+") && !s.matches("[,./:-]+"));
	}
	
	public String recognizeUnitMeasure(String s){
		
		//we check the following measures:
//		Kilogram &Kg\\
//	    Hertz & Hz\\
//	    Watt &	W \\
//	    Volt & V\\
//	    Bel, Decibel & B, DB\\
//	    Inch & " \\
//	    Joule & J\\
		if(s.matches("Kilogram")){
			return "Kilogram";
		}
		if(s.matches("kilogram")){
			return "Kilogram";
		}
		else if(s.matches("Kg")){
			return "Kilogram";
		}
		else if(s.matches("kg")){
			return "Kilogram";
		}
		else if(s.matches("Lbs")){
			return "pounds";
		}
		else if(s.matches("lbs")){
			return "pounds";
		}
		else if(s.matches("lbs.")){
			return "pounds";
		}
		else if(s.matches("Lbs.")){
			return "pounds";
		}
		else if(s.matches("Pounds")){
			return "pounds";
		}
		else if(s.matches("pounds")){
			return "pounds";
		}
		else if(s.matches("Pound")){
			return "pounds";
		}
		else if(s.matches("pound")){
			return "pounds";
		}
		else if(s.matches("Hertz")){
			return "hertz";
		}
		else if(s.matches("hertz")){
			return "hertz";
		}
		else if(s.matches("Hz")){
			return "hertz";
		}
		else if(s.matches("hz")){
			return "hertz";
		}
		else if(s.matches("Watt")){
			return "watt";
		}
		else if(s.matches("watt")){
			return "watt";
		}
		else if(s.matches("Watts")){
			return "watt";
		}
		else if(s.matches("atts")){
			return "watt";
		}
		else if(s.matches("W")){
			return "watt";
		}
		else if(s.matches("Volt")){
			return "volt";
		}
		else if(s.matches("volt")){
			return "volt";
		}
		else if(s.matches("V")){
			return "volt";
		}
		else if(s.matches("Bel")){
			return "decibel";
		}
		else if(s.matches("bel")){
			return "decibel";
		}
		else if(s.matches("Decibel")){
			return "decibel";
		}
		else if(s.matches("decibel")){
			return "decibel";
		}
		else if(s.matches("B")){
			return "decibel";
		}
		else if(s.matches("DB")){
			return "decibel";
		}
		else if(s.matches("Inch")){
			return "inch";
		}
		else if(s.matches("inch")){
			return "inch";
		}
		else if(s.matches("Inches")){
			return "inch";
		}
		else if(s.matches("inches")){
			return "inch";
		}
		else if(s.matches("\"")){
			return "inch";
		}
		else if(s.matches("Joule")){
			return "joule";
		}
		else if(s.matches("joule")){
			return "joule";
		}
		else if(s.matches("J")){
			return "joule";
		}
		else if(s.matches("kJ")){
			return "joule";
		}
		else if(s.matches("ms")){
			return "ms";
		}
		else if(s.matches("kHz")){
			return "hertz";
		}
		else if(s.matches("Hours")){
			return "hours";
		}
		else if(s.matches("hours")){
			return "hours";
		}
		else if(s.matches("hrs")){
			return "hours";
		}
		else if(s.matches("mm")){
			return "mm";
		}
		else if(s.matches("years")){
			return "years";
		}
		else if(s.matches("Years")){
			return "years";
		}
		else if(s.matches("year")){
			return "years";
		}
		else if(s.matches("Year")){
			return "years";
		}
		else if(s.matches("Nit")){
			return "Nit";
		}
		// this ones can probably be added: p, ", i,
		else{
			return "leeg";
		}

		
	}
	
	
}
