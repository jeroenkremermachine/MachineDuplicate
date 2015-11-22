package KeyTypes;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import Models.BlockSet;

public class KeyTypeRecognizer {
	
	public KeyTypeRecognizer(){
		
	}
	
	public Type createType(BlockSet bSet){
		

		if(this.recognizeString(bSet)){
			return new StringType(bSet.getKey(),"leeg");
		} else {
			String subType = this.recognizeSubType(bSet);
			return new NumericalType(bSet.getKey(), subType);
			
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
	
	public String recognizeSubType(BlockSet bSet){
		Map<String, Integer> map = new HashMap<>();

		for (int i = 0; i < bSet.getBlockSet().size(); i++){
			String t = bSet.getBlockSet().get(i).getSubType();
			Integer val = map.get(t);
			map.put(t, val == null ? 1 : val + 1);
		}

		Entry<String, Integer> max = null;

		for (Entry<String, Integer> e : map.entrySet()) {
			if (max == null || e.getValue() > max.getValue())
				max = e;
		}

		return max.getKey();
	}

}
