package Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/**
 * Class to model a set of properties (= key-value pairs here) 
 * It allows for easy access to the total set of different values of the key of the property
 * 
 * @author Thomas
 *
 */
public class ValueSet {

	private ArrayList<Value> vList;
	private String key;
	private int size;
	
	public ValueSet(String key, ArrayList<Value> list){
		
		this.key = key;
		this.vList = list;		
	}
	
	public ArrayList<Value> getValueSet(){
		return vList;
	}
	
	
	public String getKey(){
		return key;
	}
	public int size(){
	    for (Value vv : vList){
	        size++;
	       }
	       return size;
	   }
	
}

