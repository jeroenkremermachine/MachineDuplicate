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
		ArrayList<Double> f1All=new ArrayList<Double>();
		ArrayList<Double> precisionAll=new ArrayList<Double>();
		ArrayList<Double> recallAll=new ArrayList<Double>();
		ArrayList<Double> thresholdAll=new ArrayList<Double>();
		ArrayList<Double> nameThresholdAll=new ArrayList<Double>();
		ArrayList<Double> nameWeightAll=new ArrayList<Double>();
		ArrayList<Double> stringWeightAll=new ArrayList<Double>();
		ArrayList<Double> divWeightAll=new ArrayList<Double>();
		ArrayList<Double> covWeightAll=new ArrayList<Double>();
		ArrayList<Double> unitWeightAll=new ArrayList<Double>();
		ArrayList<Double> subTypeWeightAll=new ArrayList<Double>();
		ArrayList<Double> doubleWeightAll=new ArrayList<Double>();
		
		long start_time=System.currentTimeMillis();
		
		// Aanmaken van een een aantal classes
		DataReader io = new DataReader();
		long total_cycle=0;
		int nrBootstraps=7;
		double trainingPercentage=1;
		// parameters to decide if a match is sufficiently good
		
		Double[] similarityThresholds = new Double[]{1.0}; // min score to be considered a pair
		Double[] nameSimilarityThresholds = new Double[]{0.0,0.1,0.5}; // for testing purposes

		// Weights of the obtained score (yet only the standard weights are used)

		Double[] name_weights           =new Double[]{0.3,0.4,0.5,0.6,0.7,0.8,0.9};//1.1 of 1 of 0.9
		Double[] double_weights         =new Double[]{0.0,0.1,0.2,0.3,0.4,0.5,0.6};//2 ook 1.75-2.5
		Double[] string_weights         =new Double[]{0.3,0.4,0.5,0.6,0.7,0.8,0.9};//2 ook 2-2.5
		Double[] cov_weights            =new Double[]{0.0,0.1,0.2,0.3};//0.5 ook 0.2 -0.8
		Double[] div_weights            =new Double[]{0.0,0.2,0.4};//0.5 ook 0-0.5
		Double[] unit_weights           =new Double[]{0.0,0.1,0.2,0.3};//0.5 alles
		Double[] subType_weights		=new Double[]{0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9};
		long iters=similarityThresholds.length*name_weights.length*nameSimilarityThresholds.length*double_weights.length*string_weights.length*cov_weights.length*div_weights.length*unit_weights.length*subType_weights.length;
		System.out.println(iters + " iterations per bootstrap");
		// ShopList wordt de uiteindelijke dataset waar we mee gaan werken.
		ArrayList<Product> productList = new ArrayList<Product>();
		ArrayList<Shop> ShopList = new ArrayList<Shop>();
		// De vier verschillende webshops gespecificeerd
		ArrayList<String> sites = new ArrayList<String>();
		sites.add("newegg.com");
		sites.add("bestbuy.com");
		//sites.add("amazon.com");
		//sites.add("thenerds.net");

		// Reading Golden Standard
		CSVreader obj = new CSVreader();
		ArrayList<keyPair> GSpairlist = new ArrayList<keyPair>();
		GSpairlist = obj.readfile();

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
				// (Sub)Type bepalen
				KeyTypeRecognizer keytyperecognizer = new KeyTypeRecognizer();
				Type type = keytyperecognizer.createType(bSet);
				String stringType = type.getType();	
				String subType = type.getSubType();
				// (Sub)Type instellen van key
				key.setType(stringType);
				key.setSubType(subType);
				// Unit measure instellen van key
				if(bSet.getNrUnitMeasures()>0){
					String UnitMeasure = keytyperecognizer.getUnitMeasure(bSet);
					key.setUnitMeasure(UnitMeasure);
				}

				// Blockset printen
				//if (key.getName().equals("DVI Inputs")){
				//	for(int i = 0; i < bSet.getBlockSet().size(); i++){
				//		System.out.println(bSet.getBlockSet().get(i).getBlock());
				//	}
				//	System.out.println(key.getType());
				//	System.out.println(key.getSubType());
				//}


				// laden van alle doubles in de Key's Double list
				KeyDoubleFinder kdf = new KeyDoubleFinder();
				if (key.getType() == "Numerical") {
					key.addsplitList(kdf.getDoubles(bSet));
					key.addUniqueSplitList(kdf.getUniqueDoubles(key.getsplitList()));
					key.addstdv(kdf.getStdvDoubles(key.getsplitList()));
				}

				key.addStripString(kdf.getStripString(bSet));
				key.addUniqueStripString(kdf.getUniqueStripString(key.getStripString()));
				key.addDiversity(kdf.getUniqueValues(vSet)/shop.getNrProducten());
				key.addCoverage((double) vSet.size() / shop.getNrProducten());
			}

		}

		for (int boot=0; boot<nrBootstraps; boot++){
			Random rand = new Random();
			int GS_size=GSpairlist.size();
			
			int trainingDuplicates=(int) Math.floor(GS_size*trainingPercentage);

			ArrayList<Key>trainingFirst=new ArrayList<Key>(); // trainingsset first webshop
			ArrayList<Key>trainingSecond=new ArrayList<Key>();	//trainingsset second webshop

			ArrayList<Key>testingFirst=new ArrayList<Key>();	// testset first webshop	
			ArrayList<Key>testingSecond=new ArrayList<Key>();	// testset second webshop

			ArrayList<keyPair>GSTraining=new ArrayList<keyPair>(); // Golden standart for training set	
			ArrayList<keyPair>GSTesting=new ArrayList<keyPair>(); // Golden standart for test set

			ArrayList<Integer> used=new ArrayList<Integer>(); // list to make sure keypairs are only picked once
			for (int i=0; i < trainingDuplicates; i++){
				int  n = rand.nextInt(GS_size);
				while (used.contains(n)){
					n = rand.nextInt(GS_size);
				}

				used.add(n);
				
				for (Key key1:ShopList.get(0).getKey()){
					if (key1.getName().equals(GSpairlist.get(n).getKey1().getName())){
						trainingFirst.add(key1);
						break;}
				}

				for (Key key2:ShopList.get(1).getKey()){
					if (key2.getName().equals(GSpairlist.get(n).getKey2().getName())){
						trainingSecond.add(key2);
						break;}
				}
				GSTraining.add(GSpairlist.get(n));    	
			}
			for (keyPair k: GSpairlist){
				if (!GSTraining.contains(k)){
					for (Key key1:ShopList.get(0).getKey()){
						if (key1.getName().equals(k.getKey1().getName())){
							testingFirst.add(key1);
							break;}
					}
					for (Key key2:ShopList.get(1).getKey()){
						if (key2.getName().equals(k.getKey2().getName())){
							testingSecond.add(key2);
							break;}
					}
			
					GSTesting.add(k);
				}

			}

			ArrayList<String> GS_shop1 =new ArrayList<String>();
			ArrayList<String> GS_shop2 =new ArrayList<String>();
			for (int i=0; i<GS_size; i++){
				GS_shop1.add(GSpairlist.get(i).getKey1().getName());
			}
			for (int i=0; i<GS_size; i++){
				GS_shop2.add(GSpairlist.get(i).getKey2().getName());
			}
			for (Key key1:ShopList.get(0).getKey()){
				if(!GS_shop1.contains(key1.getName())){
					double a=Math.random();
					if(a < trainingPercentage){
						trainingFirst.add(key1);}

					else{
						testingFirst.add(key1);
					}
				}
			}
			for (Key key2:ShopList.get(1).getKey()){
				if(!GS_shop2.contains(key2.getName())){
					double a=Math.random();
					if(a < trainingPercentage){
						trainingSecond.add(key2);}
					else{
						testingSecond.add(key2);
					}
				}
			}
			//created training and test set
			

			long start_time_bootstrap=System.currentTimeMillis();
			// Calculating all metrics for all possible keypairs		
			ArrayList<keyPair> possibleKeyPairs = new ArrayList<keyPair>();
			for (Key key1 : trainingFirst){
				for (Key key2 : trainingSecond){
					keyPair tempKeyPair = new keyPair(key1, key2);
					// Are the keys of the same type?
					if(!key1.getType().equals(key2.getType())){
						continue;
					}
					// Calculate the different metrics and assign them to the Possible keypairs
					double nameScore = metricAlg.similarity(key1.getName(), key2.getName());
					double covScore = -java.lang.Math.pow(key1.getCoverage() - key2.getCoverage(),
							2.0);
					double divScore = -java.lang.Math.pow(key1.getDiversity(),key2.getDiversity());
					tempKeyPair.setNameScore(nameScore);
					tempKeyPair.setCovScore(covScore);
					tempKeyPair.setDivScore(divScore);

					if (key1.getType() == "String") {
						double stringScore = metricAlg.jaccard_similarity(key1.getUniqueStripString(),
								key2.getUniqueStripString());
						double isString = 0.4;
						tempKeyPair.setStringScore(stringScore);
						tempKeyPair.setIsString(isString);
					} else {
						// double p = TT.getp(key1.getUniquesplitList(),
						//		key2.getUniquesplitList());
						double jacardi = metricAlg.getJacardSimilarityDouble(
								key1.getUniquesplitList(), key2.getUniquesplitList());
						double doubleScore = java.lang.Math.max(0, jacardi);
						tempKeyPair.setDoubleScore(doubleScore);
					}
					if(key1.getSubType().equals(key2.getSubType())&& !key1.getSubType().equals("leeg")){
						double subTypeScore = 1;
						tempKeyPair.setSubTypeScore(subTypeScore);
					}

					// Calculating the unit score
					double unitScore = 0.0;
					if (key1.getUnitMeasure() == null || key2.getUnitMeasure() == null){
						unitScore = 0.0;
					}
					else if (key1.getUnitMeasure() == key2.getUnitMeasure()){
						unitScore = 1.0;
					} else {
						unitScore = -1.0;
					}
					tempKeyPair.setUnitScore(unitScore);


					// Is the keypair in the golden standard?
					boolean isGolden = false;
					
					for (keyPair GSPair : GSTraining){
						
						if (GSPair.equals(tempKeyPair)){
							isGolden = true;
							break;
						}
					}

					tempKeyPair.setIsGolden(isGolden);

					possibleKeyPairs.add(tempKeyPair);
				}
			}

			// All scores that can be obtained

			Map<String, Double> best_params = new HashMap<String, Double>();
			ArrayList<Alignments> bestAlignments = new ArrayList<Alignments>();




			double highest_f1 = 0.0;
			int counter = 0;
			total_cycle=0;
			for (double nameSimilarityThreshold:nameSimilarityThresholds){
				for (double nameScoreWeight : name_weights){
					for (double covScoreWeight : cov_weights){ 
						for (double divScoreWeight : div_weights){ 
							for (double unitScoreWeight : unit_weights){ 
								for (double stringScoreWeight : string_weights){ 
									for (double doubleScoreWeight : double_weights){
										for (double similarityThreshold : similarityThresholds){
											for (double subTypeWeight : subType_weights){
												// Object List with all the combinations of two different shops
												total_cycle=0;
												long cycle_start=System.currentTimeMillis();
												ArrayList<Alignments> allAlignments = new ArrayList<Alignments>();
												counter++;
												//System.out.println(counter);

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

																for (keyPair CurrentKeyPair : possibleKeyPairs) {

																	if (!assignedKeys1.contains(CurrentKeyPair.getKey1())) {

																		Key bestMatchingKey2 = null; // what is the best
																		// match with
																		// the current
																		// k1 and all k2
																		double highestKey2Score = -1.0;

																		if (!assignedKeys2.contains(CurrentKeyPair.getKey2())) {
																			double finalScore = -1;
																			double nameScore = CurrentKeyPair.getNameScore();
																			double doubleScore = CurrentKeyPair.getDoubleScore();
																			double stringScore = CurrentKeyPair.getStringScore();
																			double subTypeScore = CurrentKeyPair.getSubTypeScore();
																			double covScore = CurrentKeyPair.getCovScore();
																			double divScore = CurrentKeyPair.getDivScore();
																			double unitScore = CurrentKeyPair.getUnitScore();
																			double isString = CurrentKeyPair.getIsString();

																			if(nameScore>nameSimilarityThreshold){
																				finalScore = nameScore * nameScoreWeight + covScore * covScoreWeight
																						+ divScore * divScoreWeight + stringScore * stringScoreWeight
																						+ doubleScore * doubleScoreWeight + unitScoreWeight * unitScore
																						+ subTypeScore * subTypeWeight;
																				// System.out.println(CurrentKeyPair.getKey1().getName() + " en " + CurrentKeyPair.getKey2().getName()+ " hebben een final score van " + finalScore);
																			}	else {
																				continue;
																			}

																			//if(CurrentKeyPair.getKey1().getName()=="Energy Star Compliant" && CurrentKeyPair.getKey2().getName()=="REnergy Star Compliant"){
																			//	System.out.println(finalScore+" namescore" +nameScore+" stringscore " +doubleScore);
																			//}



																			if (finalScore > highestKey2Score) {
																				highestKey2Score = finalScore;
																				bestMatchingKey2 = CurrentKeyPair.getKey2();
																			}
																		}


																		if (highestKey2Score > highestPairScore) {
																			highestPairScore = highestKey2Score;
																			bestPairKey1 = CurrentKeyPair.getKey1();
																			bestPairKey2 = bestMatchingKey2;
																		}
																	}
																}

																if (highestPairScore >= similarityThreshold) {
																	keyPair newKeyPair = new keyPair(bestPairKey1, bestPairKey2);
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

												ArrayList<keyPair> foundAlignments = allAlignments.get(0).getKeyPairList();

												// Loop over all possible key pairs
												for (keyPair tempKeyPair : possibleKeyPairs){

													boolean isAligned = false;
													boolean isGolden = false;

													// If a keypair is also aligned set isAligned to true
													for (keyPair AlignedPair : foundAlignments){
														if (AlignedPair.equals(tempKeyPair)){
															isAligned = true;
														}
													}

													// If a keypair is also in the golden standard set isGolden to true
													isGolden = tempKeyPair.getIsGolden();

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


												// Calculate the different metrics
												double recall = tp / (tp + fn);
												double precision = tp / (tp + fp);
												double f1Measure = 2 * tp / (2 * tp + fp + fn);
												//System.out.println(f1Measure);



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
													best_params.put("subTypeWeight", subTypeWeight );
													best_params.put("Name Threshold", nameSimilarityThreshold);
													bestAlignments = allAlignments;
													
													
													
												}
												long cycle_end=System.currentTimeMillis();
												total_cycle=total_cycle+(cycle_end-cycle_start);
											}
										}
									}
								}
							}
						}
					}
				}
			}
			double meancycle = ((double) total_cycle/iters); 
			long end_time_bootstrap=System.currentTimeMillis();
			long bootstrap_time=(end_time_bootstrap-start_time_bootstrap)/60000;
			
			System.out.println("De hoogste F-score behaald is: "+ highest_f1 +" bootstrap" + boot+" The time spend = "+ bootstrap_time + " minutes, the mean cycle time " + meancycle + "   "+possibleKeyPairs.size() +" keypairs" );

			if(trainingPercentage<1){

			// In the next tester you can see what the algorithm has produced. Only
			// alignment 0 performs pretty well
			// this can be attributed to the fact that the Weights of scores are
			// fitted for the first combination of shops
			ArrayList<keyPair> possibleKeyPairsTest = new ArrayList<keyPair>();
			for (Key key1 : testingFirst){
				for (Key key2 : testingSecond){
					keyPair tempKeyPair = new keyPair(key1, key2);
					// Are the keys of the same type?
					if(!key1.getType().equals(key2.getType())){
						continue;
					}
					// Calculate the different metrics and assign them to the Possible keypairs
					double nameScore = metricAlg.similarity(key1.getName(), key2.getName());
					double covScore = -java.lang.Math.pow(key1.getCoverage() - key2.getCoverage(),2.0);
					double divScore = -java.lang.Math.pow(key1.getDiversity(),key2.getDiversity());
					tempKeyPair.setNameScore(nameScore);
					tempKeyPair.setCovScore(covScore);
					tempKeyPair.setDivScore(divScore);

					if (key1.getType() == "String") {
						double stringScore = metricAlg.jaccard_similarity(key1.getUniqueStripString(),
								key2.getUniqueStripString());
						double isString = 0.4;
						tempKeyPair.setStringScore(stringScore);
						tempKeyPair.setIsString(isString);
					} else {
						// double p = TT.getp(key1.getUniquesplitList(),
						//		key2.getUniquesplitList());
						double jacardi = metricAlg.getJacardSimilarityDouble(
								key1.getUniquesplitList(), key2.getUniquesplitList());
						double doubleScore = java.lang.Math.max(0, jacardi);
						tempKeyPair.setDoubleScore(doubleScore);	
					}
					if(key1.getSubType().equals(key2.getSubType()) && !key1.getSubType().equals("leeg")){
						double subTypeScore = 1;
						tempKeyPair.setSubTypeScore(subTypeScore);
					}

					// Calculating the unit score
					double unitScore = 0.0;
					if (key1.getUnitMeasure() == null || key2.getUnitMeasure() == null){
						unitScore = 0.0;
					}
					else if (key1.getUnitMeasure() == key2.getUnitMeasure()){
						unitScore = 1.0;
					} else {
						unitScore = -1.0;
					}
					tempKeyPair.setUnitScore(unitScore);


					// Is the keypair in the golden standard?
					boolean isGolden = false;
					
					for (keyPair GSPair : GSTesting){
						
						if (GSPair.equals(tempKeyPair)){
							isGolden = true;
							continue;
						}
					}

					tempKeyPair.setIsGolden(isGolden);

					possibleKeyPairsTest.add(tempKeyPair);
				}
			}

			// All scores that can be obtained



			// parameters to decide if a match is sufficiently good
			Double nameSimilarityThresholdsTest = best_params.get("Name Threshold"); // for testing purposes
			Double similarityThresholdTest = best_params.get("Similarity Threshold"); // min score to be considered a pair


			// Weights of the obtained score (yet only the standard weights are used)

			Double name_weightTest         	 =best_params.get("nameScoreWeight");
			Double double_weightTest         =best_params.get("doubleScoreWeight");
			Double string_weightTest         =best_params.get("stringScoreWeight");
			Double cov_weightTest            =best_params.get("covScoreWeight");
			Double div_weightTest            =best_params.get("divScoreWeight");
			Double unit_weightTest           =best_params.get("unitScoreWeight");
			Double subType_weightTest		 =best_params.get("subTypeWeight");
			
			thresholdAll.add(similarityThresholdTest);
			nameThresholdAll.add(nameSimilarityThresholdsTest);
			nameWeightAll.add(name_weightTest);
			stringWeightAll.add(string_weightTest);
			divWeightAll.add(div_weightTest);
			covWeightAll.add(cov_weightTest);
			unitWeightAll.add(unit_weightTest );
			subTypeWeightAll.add(subType_weightTest);
			doubleWeightAll.add(double_weightTest);

		
			
												// Object List with all the combinations of two different shops
												ArrayList<Alignments> allAlignments = new ArrayList<Alignments>();
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

																for (keyPair CurrentKeyPair : possibleKeyPairsTest) {

																	if (!assignedKeys1.contains(CurrentKeyPair.getKey1())) {

																		Key bestMatchingKey2 = null; // what is the best
																		// match with
																		// the current
																		// k1 and all k2
																		double highestKey2Score = -1.0;

																		if (!assignedKeys2.contains(CurrentKeyPair.getKey2())) {
																			double finalScore = -1;
																			double nameScore = CurrentKeyPair.getNameScore();
																			double doubleScore = CurrentKeyPair.getDoubleScore();
																			double stringScore = CurrentKeyPair.getStringScore();
																			double subTypeScore = CurrentKeyPair.getSubTypeScore();
																			double covScore = CurrentKeyPair.getCovScore();
																			double divScore = CurrentKeyPair.getDivScore();
																			double unitScore = CurrentKeyPair.getUnitScore();
																			double isString = CurrentKeyPair.getIsString();
																		
																			if(nameScore>nameSimilarityThresholdsTest){
																				finalScore = nameScore * name_weightTest + covScore * cov_weightTest
																						+ divScore * div_weightTest + stringScore * string_weightTest
																						+ doubleScore * double_weightTest  + unit_weightTest * unitScore
																						+ subTypeScore * subType_weightTest;
																				// System.out.println(CurrentKeyPair.getKey1().getName() + " en " + CurrentKeyPair.getKey2().getName()+ " hebben een final score van " + finalScore);
																			}	else {
																				continue;
																			}

																			//if(CurrentKeyPair.getKey1().getName()=="Energy Star Compliant" && CurrentKeyPair.getKey2().getName()=="REnergy Star Compliant"){
																			//	System.out.println(finalScore+" namescore" +nameScore+" stringscore " +doubleScore);
																			//}



																			if (finalScore > highestKey2Score) {
																				highestKey2Score = finalScore;
																				bestMatchingKey2 = CurrentKeyPair.getKey2();
																			}
																		}


																		if (highestKey2Score > highestPairScore) {
																			highestPairScore = highestKey2Score;
																			bestPairKey1 = CurrentKeyPair.getKey1();
																			bestPairKey2 = bestMatchingKey2;
																		}
																	}
																}

																if (highestPairScore >= similarityThresholdTest) {
																	keyPair newKeyPair = new keyPair(bestPairKey1, bestPairKey2);
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

												ArrayList<keyPair> foundAlignments = allAlignments.get(0).getKeyPairList();

												// Loop over all possible key pairs
												for (keyPair tempKeyPair : possibleKeyPairsTest){

													boolean isAligned = false;
													boolean isGolden = false;

													// If a keypair is also aligned set isAligned to true
													for (keyPair AlignedPair : foundAlignments){
														if (AlignedPair.equals(tempKeyPair)){
															isAligned = true;
														}
													}

													// If a keypair is also in the golden standard set isGolden to true
													isGolden = tempKeyPair.getIsGolden();

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


												// Calculate the different metrics
												double recall = tp / (tp + fn);
												double precision = tp / (tp + fp);
												double f1Measure = 2 * tp / (2 * tp + fp + fn);
												//System.out.println(f1Measure);
												f1All.add(f1Measure);
												precisionAll.add(precision);
												recallAll.add(recall);


			System.out.println("De F-score in de testSet is: "+ f1Measure);
			//System.out.println("met de volgende parameters" + best_params);
			
			
		}
		}
		long end_time=System.currentTimeMillis();
		long time=(end_time-start_time)/60000;
		System.out.println("Total time spend = "+ time + " minutes");
		String fileName= "out.txt";

		try{
			PrintWriter outputStream = new PrintWriter(fileName);
			

			outputStream.println("f1Measure");
				for(double p:f1All){
					outputStream.println(p);
				}
			outputStream.println("recall");	
			for(double p:recallAll){
				outputStream.println(p);
			}
			outputStream.println("precision");	
			for(double p:precisionAll){
				outputStream.println(p);
			}
			outputStream.println("Threshold");	
			for(double p:thresholdAll){
				outputStream.println(p);
			}
			outputStream.println("nameThreshold");	
			for(double p:nameThresholdAll){
				outputStream.println(p);
			}
			outputStream.println("nameWeight");	
			for(double p:nameWeightAll){
				outputStream.println(p);
			}
			outputStream.println("stringWeight");	
			for(double p:stringWeightAll){
				outputStream.println(p);
			}
			outputStream.println("doubleWeight");
			for(double p:doubleWeightAll){
				outputStream.println(p);
			}
			outputStream.println("covWeight");	
			for(double p:covWeightAll){
				outputStream.println(p);
			}
			outputStream.println("divWeight");	
			for(double p:divWeightAll){
				outputStream.println(p);
			}
			outputStream.println("unitWeight");	
			for(double p:unitWeightAll){
				outputStream.println(p);
			}
			outputStream.println("subTypeWeight");	
			for(double p:subTypeWeightAll){
				outputStream.println(p);
			}
			
			outputStream.close();
		}
		catch (FileNotFoundException e){
			e.printStackTrace();

		}
	}
}
