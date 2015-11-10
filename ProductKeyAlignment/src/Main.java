import java.util.*;
import Models.*;
import KeyTypes.*;
import KeyTypes.KeyDoubleFinder;
import Comparison.*;

public class Main {

	public static void main(String[] args) {
		/*
		 * Reading the data
		 * HALLO
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
					for (Key mm : KeyList) {
						if (mm.getName() == k) {
							// Toevoegen van de nieuwe value aan de juiste key
							mm.addValue(v);
							isNewKey = false;
						}
					}
					// Als we een nieuwe key hebben gevonden, maken we deze aan
					// en voegen we hem toe aan de KeyList
					if (isNewKey) {
						Key p = new Key(k, v);
						KeyList.add(p);
					}
				}
			}
			// System.out.println("webshop: "+ site + " has "+ productList.size()
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
				// Arie: wil je dit niet alleen doen als de key van type String is?
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

		/*
		 * Nu heeft elke Key dus ook een string Type die ofwel de waarde
		 * "String" ofwel de waarde "Double1" heeft en aangeeft van welk
		 * datatype de Key is. Je kunt deze waarde krijgen door de methode
		 * key.getType() te gebruiken
		 */
		// scores of comparing 2 keys
		double nameScore = 0;
		double doubleScore = 0;
		double stringScore = 0;
		double covScore = 0;
		double divScore = 0;
		double unitScore = 0;
		double isString = 0;

		// parameters
		double similarityThreshold = 0;
		double minNameScore = 0;
		double stringBonus = 0;
		double minContainedScore = 0;

		// two string to compare for testing the initial algorithm
		Key k1 = ShopList.get(0).getKey().get(3);
		Key k2 = ShopList.get(1).getKey().get(15);
		boolean score = true;
		if (k1.getType() == k2.getType()) {
			nameScore = metricAlg.similarity(k1.getName(), k2.getName());
			covScore = -java.lang.Math.pow(k1.getCoverage() - k2.getCoverage(), 2.0);
			divScore = metricAlg.diversit(k1.getDiversity(), k2.getDiversity());
			if (k1.getType() == "String") {
				stringScore = metricAlg.jaccard_similarity(k1.getUniqueStripString(), k2.getUniqueStripString());
				isString = 1;
			} else {
				double p = TT.getp(k1.getUniquesplitList(), k2.getUniquesplitList());
				double jacardi = metricAlg.getJacardSimilarityDouble(k1.getUniquesplitList(), k2.getUniquesplitList());
				doubleScore = java.lang.Math.max(p, jacardi);
			}
		} else {
			score = false;
		}
		System.out.println("we vergelijken de keys : " + k1.getName() + " en  " + k2.getName());
		System.out.println("deze key zijn van zelfde type " + score + ", de naam score = " + nameScore
				+ " the divScore = " + divScore);
		System.out.println(
				"de covScore = " + covScore + " de stringScore = " + stringScore + " de doubleScore " + doubleScore);

	}

}