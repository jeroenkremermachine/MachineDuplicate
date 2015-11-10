import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import Models.Product;
import Models.Property;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;

/*
 * This file reads the data from a JSON file 
 *
 */

public class DataReader {
	
	private static ArrayList<Product> productList = new ArrayList<Product>();

	public ArrayList<Product> fetch(String[] shopSelection){
		
		try {
			 
			JsonFactory jfactory = new JsonFactory();
			String path = "./data/TVs-all-merged.json"; 
			
			/*** read from file ***/
			@SuppressWarnings("deprecation")
			JsonParser jParser = jfactory.createJsonParser(new File(path));

			ArrayList<String> preferredShops = new ArrayList<String>();
			for(String shop : shopSelection){
				preferredShops.add(shop);
			}
		
			while (jParser.isClosed() == false) {
				jParser.nextToken();
				String key = jParser.getCurrentName();	
			    String value = jParser.getValueAsString();	
			    
			    boolean isNewProperty = key != null && value != null;
			    boolean isNewProduct = key == "shop";
			    boolean productIsFromPreferredShop = false;
			    if(isNewProduct) productIsFromPreferredShop = preferredShops.contains(value);
			    
			    if(isNewProperty && isNewProduct && productIsFromPreferredShop) {			    	
		    		Product p = new Product(); // create new product		    		
		    		while(true){
		    			
		    			if(isNewProduct){
		    				p.addProperty(new Property(key,value)); // add property
		    				isNewProduct = false;
		    			} 
		    			else{
		    				jParser.nextToken();
							key = jParser.getCurrentName();	
						    value = jParser.getValueAsString();	
						    isNewProperty = key != null && value != null;
						    if(isNewProperty) p.addProperty(new Property(key,value)); // add property
						    
						    if(jParser.getCurrentName() == "title" && isNewProperty) {
			    				productList.add(p);
			    				break;
			    			}
		    			}
		    		}			    	
			    }
			    
			}
			
			  jParser.close();
		 
		     } catch (JsonGenerationException e) {
		 
			  e.printStackTrace();
		 
		     } catch (IOException e) {
		 
			  e.printStackTrace();
		 
		     }
		
		return productList;
	}
	
	
	
	
	
}
