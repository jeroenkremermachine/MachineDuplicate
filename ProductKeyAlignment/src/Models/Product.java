package Models;

import java.util.ArrayList;
import Models.Property;

public class Product {
	
	private ArrayList<Property> propertyList = new ArrayList<Property>();
	
	public Product(){
		
	}
	
	public Product(ArrayList<Property> propList){
		propertyList = propList;
	}

	public void addProperty(Property p){
		propertyList.add(p);
	}

	public ArrayList<Property> getPropertyList() {
		return propertyList;
	}
		
}
