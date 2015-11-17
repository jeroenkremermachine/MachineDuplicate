package KeyTypes;

import Models.BlockSet;

public class KeyTypeRecognizer {
	
	public KeyTypeRecognizer(){
		
	}
	
	public Type createType(BlockSet bSet){
		

		if(this.recognizeString(bSet)){
			return new StringType(bSet.getKey());
		} else {
			return new Double1Type(bSet.getKey());
			
		}

		
	}
	
	public String getUnitMeasure(BlockSet bSet){
		return bSet.getUnitMeasure();
	}
	
	public boolean recognizeString(BlockSet bSet){
		if(bSet.getNrDoubles() + bSet.getNrUnitMeasures() <= bSet.getNrStrings()){
			return true;
		} else {
			return false;
		}
	}

}
