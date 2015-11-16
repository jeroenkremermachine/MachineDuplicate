
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import Models.Key;
import Models.KeyNamePair;
import Models.keyPair;





public class CSVreader {



	  public ArrayList<KeyNamePair> readfile() {

		String csvFile = "./data/GS.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		ArrayList<KeyNamePair> GSnamepairlist = new ArrayList<KeyNamePair>(); 
		
		try {

			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

			        // use comma as separator
				String[] columns = line.split(cvsSplitBy);
				String keyname1 = columns[1];
				String keyname2 = columns[3];
				
				
				KeyNamePair GSpair = new KeyNamePair(keyname1,keyname2);			
				GSnamepairlist.add(GSpair);
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
	  
		return GSnamepairlist;
	  }
	  
}
	
	

