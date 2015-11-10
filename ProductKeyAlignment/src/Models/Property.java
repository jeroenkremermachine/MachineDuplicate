package Models;

import java.util.HashMap;
import KeyTypes.*;

public class Property {
	private String key;
	private String value;
	private String type;
	private ValueSet valueSet;
	private String typePredicted;
	private String typeGolden;
	private BlockSet bSet;	
	
	public Property(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}
	
	
	
	
	public String getKey() {
		return key;
	}



	public void setKey(String key) {
		this.key = key;
	}



	public String getValue() {
		return value;
	}



	public void setValue(String value) {
		this.value = value;
	}




	
	
	
	
}
