package Models;

import java.util.ArrayList;

public class Alignments {
	private Shop shop1;
	private Shop shop2;
	private ArrayList <keyPair> pairs;
    public Alignments(Shop a, Shop b)
    
    {
    		shop1=a;
    		shop2=b;
    		pairs = new ArrayList<keyPair>();
    }
    public void addKeyPair(keyPair newfound)
    {
    	pairs.add(newfound);
    }
    public ArrayList<keyPair> getKeyPairList(){
    return pairs;
    }
    public Shop getFirstShop(){
    return shop1;
    }

	public Shop getSecondShop(){
		return shop2;
}
}
