import java.util.*;
import Models.*;
import KeyTypes.*;
import KeyTypes.KeyDoubleFinder;
import Comparison.*;

public class Main {
	
    public static Map<String, Double> best_params = new HashMap();

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
		sites.add("amazon.com");
		sites.add("thenerds.net");

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

				// laden van alle doubles in de Key's Double list
				KeyDoubleFinder kdf = new KeyDoubleFinder();
				if (key.getType() == "Double1") {
					key.addsplitList(kdf.getDoubles(bSet));
					key.addUniqueSplitList(kdf.getUniqueDoubles(key.getsplitList()));
					key.addstdv(kdf.getStdvDoubles(key.getsplitList()));
				}
				// Arie: wil je dit niet alleen doen als de key van type String
				// is?
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
		// Object List with all the combinations of two different shops
		ArrayList<Alignments> allAlignments = new ArrayList<Alignments>();
		// All scores that can be obtained

		// parameters to decide if a match is sufficiently good
		double minNameScore = 0.0; // not yet used
		double similarityThreshold = 1.6; // min score to be considered a pair

		// Weights of the obtained score (yet only the standard weights are used)

		
        Double[] key_weights            =new Double[]{0.9};//1.1 of 1 of 0.9
        Double[] double_weights         =new Double[]{1.7};//2 ook 1.75-2.5
        Double[] string_weights         =new Double[]{2.0};//2 ook 2-2.5
        Double[] cov_weights            =new Double[]{0.3};//0.5 ook 0.2 -0.8
        Double[] div_weights            =new Double[]{0.0};//0.5 ook 0-0.5
        Double[] unit_weights           =new Double[]{0.5};//0.5 alles
        double highest_f1 = 0.0;
   
            for (double key_weight : key_weights){
                        for (double cov_weight : cov_weights){ 
                            for (double div_weight : div_weights){ 
                                for (double unit_weight : unit_weights){ 
                                        for (double string_weight : string_weights){ 
                                                for (double double_weight : double_weights){ 
                                                // dit stukje is een beetje overbodig natuurlijk, maar dit los ik maandag op met jullie hulp. 
                                            		double nameScoreWeight = key_weight; 
                                            		double doubleScoreWeight = double_weight;
                                            		double stringScoreWeight = string_weight;
                                            		double covScoreWeight = cov_weight;
                                            		double divScoreWeight = div_weight;
                                            		double unitScoreWeight = unit_weight;
                                            		
		ArrayList<Shop> checkedShops = new ArrayList<Shop>(); // checked welke
																// shops al een
																// keer zijn
																// vergeleken

		/*
		 * boolean assigning=true;
		 * 
		 * Key bestPairKey1=null; Key bestPairKey2=null; Double highestPairScore
		 * = -1.0; Key bestMatchingKey2= null; Double highestKey2Score= -1.0;
		 * Key bestMatchingKey2= null;
		 */

		for (Shop shop1 : ShopList) { // check all combinations of shops in the
										// ShopList
			checkedShops.add(shop1);
			for (Shop shop2 : ShopList) {
				if (!checkedShops.contains(shop2)) { // check if the current
														// combination of shops
														// isnt checked allready
					// next line can be used to see wich combination of shops
					// are considered
					// System.out.println( "shop 1 : " + shop1.getName() + "
					// wordt vergeleken met shop: " +shop2.getName() );
					// make a new allignment object which contains 2 shops and a
					// list with keymatches
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
										// double unitScore = 0;
										double isString = 0;
										if (key1.getType() != key2.getType()) {
											break;
										} else {

											nameScore = metricAlg.similarity(key1.getName(), key2.getName());
											// if (nameScore < minNameScore)
											// {break;} // In nicks code it says
											// the following:
											// if(key_score<min_key_score){continue;}
											// //too risky
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
												// System.out.println("deze
												// string is van type double" +
												// key2.getName()); // A lot
												// keys are considered strings
												// while they are doubles
											}
											finalScore = nameScore * nameScoreWeight + covScore * covScoreWeight
													+ divScore * divScoreWeight + stringScore * stringScoreWeight
													+ doubleScore * doubleScoreWeight + isString;
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
									// System.out.println(highestPairScore + "
									// hoogste tot nog toe " +
									// bestPairKey1.getName() + " met " +
									// bestPairKey2.getName());
								}
							}
						}
						if (highestPairScore >= similarityThreshold) {
							// System.out.println("de Key :" +
							// bestPairKey1.getName() + " is gematcht met: "+
							// bestPairKey2.getName() + " met een score van : "
							// + highestPairScore);
							keyPair newKeyPair = new keyPair(bestPairKey1, bestPairKey2, highestPairScore);
							current.addKeyPair(newKeyPair);
							assigning = true;
							assignedKeys1.add(bestPairKey1);
							assignedKeys2.add(bestPairKey2);
						} else {
							// threshold is probably to high, if you uncomment
							// next line you can see that a lot of reasonable
							// options are declined
							// System.out.println(bestPairKey1.getName() +" " +
							// bestPairKey1.getName() +" "+ highestPairScore);
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
			
		// Loop over al possible key pairs
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
		
		// Print the different metrics, can be deleted if necessary
		System.out.println("The recall is " + recall);
		System.out.println("The precision is " + precision);
		System.out.println("The f1Measure is " + f1Measure);

/*
                                                            if (f1Measure > highest_f1){ //highest f1 aanmaken nog
                                                                highest_f1=f1Measure;
                                                                best_params.clear();
                                                                //weights
                                                                best_params.put("key_weight", key_weight );
                                                                best_params.put("double_weight", double_weight );
                                                                best_params.put("string_weight", string_weight );
                                                                best_params.put("cov_weight", cov_weight );
                                                                best_params.put("div_weight", div_weight );
                                                                best_params.put("unit_weight",unit_weight );
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
									}	
	


/*
		// In the next tester you can see what the algorithm has produced. Only
		// alignment 0 performs pretty well
		// this can be attributed to the fact that the Weights of scores are
		// fitted for the first combination of shops
		Alignments tester = allAlignments.get(0);
		System.out.println("in this alignment the keys of the shops: " + tester.getFirstShop().getName() + " and: "
				+ tester.getSecondShop().getName() + " are compared");
		System.out.println("Willemijn is echt retegeil!");		
		for (keyPair pp : tester.getKeyPairList()) {
			System.out.println("key : " + pp.getKey1().getName() + " is matched with key: " + pp.getKey2().getName());

		}

		/*
		 * Nu heeft elke Key dus ook een string Type die ofwel de waarde
		 * "String" ofwel de waarde "Double1" heeft en aangeeft van welk
		 * datatype de Key is. Je kunt deze waarde krijgen door de methode
		 * key.getType() te gebruiken
		 */

		/*
		 * // two string to compare for testing the initial algorithm Key k1 =
		 * ShopList.get(1).getKey().get(4); Key k2 =
		 * ShopList.get(2).getKey().get(4); boolean score = true; if
		 * (k1.getType() == k2.getType()) { nameScore =
		 * metricAlg.similarity(k1.getName(), k2.getName()); covScore =
		 * -java.lang.Math.pow(k1.getCoverage() - k2.getCoverage(), 2.0);
		 * divScore = metricAlg.diversit(k1.getDiversity(), k2.getDiversity());
		 * if (k1.getType() == "String") { stringScore =
		 * metricAlg.jaccard_similarity(k1.getUniqueStripString(),
		 * k2.getUniqueStripString()); isString = 1; } else { double p =
		 * TT.getp(k1.getUniquesplitList(), k2.getUniquesplitList()); double
		 * jacardi =
		 * metricAlg.getJacardSimilarityDouble(k1.getUniquesplitList(),
		 * k2.getUniquesplitList()); doubleScore = java.lang.Math.max(p,
		 * jacardi); } } else { score = false; }
		 * 
		 */

		/*
		 * System.out.println("we vergelijken de keys : " + k1.getName() +
		 * " en  " + k2.getName()); System.out.println(
		 * "deze key zijn van zelfde type " + score + ", de naam score = " +
		 * nameScore + " the divScore = " + divScore); System.out.println(
		 * "de covScore = " + covScore + " de stringScore = " + stringScore +
		 * " de doubleScore " + doubleScore);
		 */
        
                            /* IN ONDERSTE REGEL ZIJN DE }} PUUR OM HET PROGRAMMA TE LATEN RUNNEN
      }

}
                            */
				
 }}}}}}}}