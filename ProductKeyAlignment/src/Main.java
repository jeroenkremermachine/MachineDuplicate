import java.util.*;
import Models.*;
import KeyTypes.*;
import KeyTypes.KeyDoubleFinder;
import Comparison.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Main {
	
    

	public static void main(String[] args) {
		/*
		 * Reading the data
		 */
		
		
		// Aanmaken van een een aantal classes
		DataReader io = new DataReader();
		Ttest TT = new Ttest();
		// ShopList wordt de uiteindelijke dataset waar we mee gaan werken.
		ArrayList<Product> productList = new ArrayList<Product>();
		ArrayList<Shop> ShopList = new ArrayList<Shop>();
		// De vier verschillende webshops gespecificeerd
		ArrayList<String> sites = new ArrayList<String>();
		sites.add("bestbuy.com");
		sites.add("newegg.com");
		//sites.add("amazon.com");
		//sites.add("thenerds.net");

		// Read data from each webshop
		for (String site : sites) {
			// Clear de lijsten producten en keys voor de nieuwe webshop
			ArrayList<Key> KeyList = new ArrayList<Key>();
			productList.clear();

			// Haal de producten op uit het JSON-file voor de betreffende
			// webshop
			productList = io.fetch(new String[] { site });

			// Hier loopen we over elke product en elke property
			for (Product pr : productList) {
				for (Property prop : pr.getPropertyList()) {
					boolean isNewKey = true;
					String k = prop.getKey();
					String v = prop.getValue();
					if (k != "shop" && k != "url" && k != "modelID" && k != "title") {
						for (Key mm : KeyList) {
							if (mm.getName() == k) {
								// Toevoegen van de nieuwe value aan de juiste
								// key
								mm.addValue(v);
								isNewKey = false;
							}
						}
						// Als we een nieuwe key hebben gevonden, maken we deze
						// aan
						// en voegen we hem toe aan de KeyList
						if (isNewKey) {
							Key p = new Key(k, v);
							KeyList.add(p);
						}
					}
				}
			}
			// System.out.println("webshop: "+ site + " has "+
			// productList.size()
			// +"producten");
			// Toevoegen van de webshop aan de dataset ShopList
			Shop tempShop = new Shop(site, KeyList, productList.size());

			ShopList.add(tempShop);

		}
		/*
		 * De uiteindelijke dataset die we gebruiken is ShopList. Dit is een
		 * ArrayList met de vier shopnamen en al hun bijbehorende KeyLists. Een
		 * KeyList is een ArrayList van AL hun bijbehorende values in de
		 * betreffende shop
		 */

		/*
		 * In deze sectie bepalen we voor elke Key zijn datatype door zijn
		 * values op te delen in blocks en daaruit te bepalen welk datatype het
		 * meest waarschijnlijk is voor bijbehorende key
		 */

		// Loopen over elke key in onze data

		for (Shop shop : ShopList) {
			ArrayList<Key> TempKeyList = shop.getKey();
			for (Key key : TempKeyList) {
				// ValueSet van de key construeren
				ValueSet vSet = new ValueSet(key.getName(), key.getValues());
				// BlockSet van de key construeren
				BlockSet bSet = new BlockSet(vSet);
				// Type bepalen
				KeyTypeRecognizer keytyperecognizer = new KeyTypeRecognizer();
				Type type = keytyperecognizer.createType(bSet);
				String stringType = type.getType();				
				// Type instellen van key
				key.setType(stringType);
				// Unit measure instellen van key
				if(bSet.getNrUnitMeasures()>0){
					String UnitMeasure = keytyperecognizer.getUnitMeasure(bSet);
					key.setUnitMeasure(UnitMeasure);
				}

				// laden van alle doubles in de Key's Double list
				KeyDoubleFinder kdf = new KeyDoubleFinder();
				if (key.getType() == "Double1") {
					key.addsplitList(kdf.getDoubles(bSet));
					key.addUniqueSplitList(kdf.getUniqueDoubles(key.getsplitList()));
					key.addstdv(kdf.getStdvDoubles(key.getsplitList()));
				}
				
				key.addStripString(kdf.getStripString(bSet));
				key.addUniqueStripString(kdf.getUniqueStripString(key.getStripString()));
				key.addDiversity(kdf.getUniqueValues(vSet));
				key.addCoverage((double) vSet.size() / shop.getNrProducten());

				/*
				 * for(String s: key.getUniqueStripString()){
				 * 
				 * System.out.println(s + "                from key "
				 * +key.getName() +"    " + key.getDiversity() + "   "+
				 * key.getCoverage()); }
				 */

			}

		}

		Map<String, Double> best_params = new HashMap<String, Double>();
		ArrayList<Alignments> bestAlignments = new ArrayList<Alignments>();
		// All scores that can be obtained

		// parameters to decide if a match is sufficiently good
		double minNameScore = 0.0; // not yet used
		Double[] similarityThresholds = new Double[]{0.9}; // min score to be considered a pair

		
		// Weights of the obtained score (yet only the standard weights are used)

		
        Double[] key_weights            =new Double[]{0.8};//1.1 of 1 of 0.9
        Double[] double_weights         =new Double[]{1.0};//2 ook 1.75-2.5
        Double[] string_weights         =new Double[]{1.0};//2 ook 2-2.5
        Double[] cov_weights            =new Double[]{0.2};//0.5 ook 0.2 -0.8
        Double[] div_weights            =new Double[]{0.5};//0.5 ook 0-0.5
        Double[] unit_weights           =new Double[]{0.0};//0.5 alles
        double highest_f1 = 0.0;
        int counter = 0;
   
            for (double nameScoreWeight : key_weights){
                        for (double covScoreWeight : cov_weights){ 
                            for (double divScoreWeight : div_weights){ 
                                for (double unitScoreWeight : unit_weights){ 
                                        for (double stringScoreWeight : string_weights){ 
                                                for (double doubleScoreWeight : double_weights){
                                                	for (double similarityThreshold : similarityThresholds){
                                                		// Object List with all the combinations of two different shops
                                                		ArrayList<Alignments> allAlignments = new ArrayList<Alignments>();
                                                	counter++;
                                                	System.out.println(counter);
                                            		
		ArrayList<Shop> checkedShops = new ArrayList<Shop>(); // checked welke
																// shops al een
																// keer zijn
																// vergeleken


		for (Shop shop1 : ShopList) { // check all combinations of shops in the
										// ShopList
			checkedShops.add(shop1);
			for (Shop shop2 : ShopList) {
				if (!checkedShops.contains(shop2)) { // check if the current
														// combination of shops
														// isnt checked allready
					Alignments current = new Alignments(shop1, shop2);
					// add the allingment object to list so you can retrieve it
					// later on
					allAlignments.add(current);
					// are there still combinations of keys that score better
					// than 1.7
					boolean assigning = true;
					// make lists of keys that are already matched between the
					// two shops
					ArrayList<Key> assignedKeys1 = new ArrayList<Key>();
					ArrayList<Key> assignedKeys2 = new ArrayList<Key>();
					while (assigning == true) {
						// these Key are the best scoring keys that are found
						// yet
						Key bestPairKey1 = null;
						Key bestPairKey2 = null;
						Key PairKey2 = null;
						double highestPairScore = -1.0;

						for (Key key1 : shop1.getKey()) {
	
							if (!assignedKeys1.contains(key1)) {

								Key bestMatchingKey2 = null; // what is the best
																// match with
																// the current
																// k1 and all k2
								double highestKey2Score = -1.0;
								for (Key key2 : shop2.getKey()) {
						
									
									if (!assignedKeys2.contains(key2)) {
										double finalScore = -1;
										double nameScore = 0;
										double doubleScore = 0;
										double stringScore = 0;
										double covScore = 0;
										double divScore = 0;
										double unitScore = 0;
										double isString = 0;
										
										if (key1.getType() != key2.getType()) {
											break;
										} else {

											nameScore = metricAlg.similarity(key1.getName(), key2.getName());
											// if (nameScore < minNameScore)
											// {break;} 
											covScore = -java.lang.Math.pow(key1.getCoverage() - key2.getCoverage(),
													2.0);
											divScore = metricAlg.diversit(key1.getDiversity(), key2.getDiversity());
											if (key1.getType() == "String") {
												stringScore = metricAlg.jaccard_similarity(key1.getUniqueStripString(),
														key2.getUniqueStripString());
												isString = 0.4;

											} else {
												double p = TT.getp(key1.getUniquesplitList(),
														key2.getUniquesplitList());
												double jacardi = metricAlg.getJacardSimilarityDouble(
														key1.getUniquesplitList(), key2.getUniquesplitList());
												doubleScore = java.lang.Math.max(p, jacardi);
											}
											
											// Calculating the unit score
											if (key1.getUnitMeasure() == null || key2.getUnitMeasure() == null){
												unitScore = 0;
											}
											else if (key1.getUnitMeasure() == key2.getUnitMeasure()){
												unitScore = 1;
											} else {
												unitScore = -1;
											}

											finalScore = nameScore * nameScoreWeight + covScore * covScoreWeight
													+ divScore * divScoreWeight + stringScore * stringScoreWeight
													+ doubleScore * doubleScoreWeight + isString + unitScoreWeight * unitScore;
												}

									


										if (finalScore > highestKey2Score) {
											highestKey2Score = finalScore;
											bestMatchingKey2 = key2;
										}
									}
								}
								if (highestKey2Score > highestPairScore) {
									highestPairScore = highestKey2Score;
									bestPairKey1 = key1;
									bestPairKey2 = bestMatchingKey2;
								}
							}
						}
						if (highestPairScore >= similarityThreshold) {
							keyPair newKeyPair = new keyPair(bestPairKey1, bestPairKey2, highestPairScore);
							current.addKeyPair(newKeyPair);
							assigning = true;
							assignedKeys1.add(bestPairKey1);
							assignedKeys2.add(bestPairKey2);
						} else {
							assigning = false;
						}
					}
				}
			}
		}

		/*
		 * Evaluation
		 */

		// Initializing
		double tp = 0;
		double tn = 0;
		double fp = 0;
		double fn = 0;
		
		
		// Reading Golden Standard
		CSVreader obj = new CSVreader();
		ArrayList<keyPair> GSpairlist = new ArrayList<keyPair>();
		GSpairlist = obj.readfile();
		ArrayList<keyPair> foundAlignments = allAlignments.get(0).getKeyPairList();
			
		// Loop over all possible key pairs
		for (Key key1 : ShopList.get(0).getKey()) {
			for (Key key2 : ShopList.get(1).getKey()) {
				keyPair tempKeyPair = new keyPair(key1, key2,0.0);

				boolean isAligned = false;
				boolean isGolden = false;
				
				// If a keypair is also aligned set isAligned to true
				for (keyPair AlignedPair : foundAlignments){
					if (AlignedPair.equals(tempKeyPair)){
						isAligned = true;
					}
				}
				
				// If a keypair is also in the golden standard set isGolden to true
				for (keyPair GSPair : GSpairlist){
					if (GSPair.equals(tempKeyPair)){
						isGolden = true;
					}
				}
				
				// Determine if the keypair is a true/false positive/negative and count it
				if (isAligned && isGolden) {
					tp++;
				} else if (!isAligned && isGolden) {
					fn++;
				} else if (isAligned && !isGolden) {
					fp++;
				} else if (!isAligned && !isGolden) {
					tn++;
				}
			}
		}
		
		// Calculate the different metrics
		double recall = tp / (tp + fn);
		double precision = tp / (tp + fp);
		double f1Measure = 2 * tp / (2 * tp + fp + fn);
		


                                                            if (f1Measure > highest_f1){
                                                                highest_f1=f1Measure;
                                                                best_params.clear();
                                                                bestAlignments.clear();
                                                                //weights
                                                                best_params.put("Similarity Threshold", similarityThreshold);
                                                                best_params.put("nameScoreWeight", nameScoreWeight );
                                                                best_params.put("doubleScoreWeight", doubleScoreWeight );
                                                                best_params.put("stringScoreWeight", stringScoreWeight );
                                                                best_params.put("covScoreWeight", covScoreWeight );
                                                                best_params.put("divScoreWeight", divScoreWeight );
                                                                best_params.put("unitScoreWeight",unitScoreWeight );
                                                        		bestAlignments = allAlignments;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
									}	
            }
            System.out.println(highest_f1);
            System.out.println(best_params);


		// In the next tester you can see what the algorithm has produced. Only
		// alignment 0 performs pretty well
		// this can be attributed to the fact that the Weights of scores are
		// fitted for the first combination of shops
		String fileName= "out.txt";
		try{
			PrintWriter outputStream = new PrintWriter(fileName);
		
			Alignments tester = bestAlignments.get(0);
			outputStream.println("in this alignment the keys of the shops: " + tester.getFirstShop().getName() + " and: "
				+ tester.getSecondShop().getName() + " are compared");
			System.out.println("Ik heb het doorgegeven Joost en Romke!");		
				for (keyPair pp : tester.getKeyPairList()) {
					outputStream.println("key : " + pp.getKey1().getName() + " is matched with key: " + pp.getKey2().getName());
					

		}
				outputStream.close();
		}
        catch (FileNotFoundException e){
        	e.printStackTrace();

        }
		
        
 }
}