
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import Models.Key;
import Models.keyPair;





public class CSVreader {



	  public ArrayList<keyPair> readfile() {

		String csvFile = "./data/GS.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		ArrayList<keyPair> GSpairlist = new ArrayList<keyPair>(); 
		
		try {

			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

			        // use comma as separator
				String[] columns = line.split(cvsSplitBy);
				Key key1 = new Key(columns[1],"");
				Key key2 = new Key(columns[3],"");
				
				
				keyPair GSpair = new keyPair(key1,key2,0.0);			
				GSpairlist.add(GSpair);
			}
			

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	  
		return GSpairlist;
	  }
	  
}
	
	

