package Models;


public class keyPair {
	private Key key1;
	private Key key2;
	private Double score;
	
	 public keyPair(Key aaa, Key bbb, Double ccc){
	    		key1=aaa;
	    		key2=bbb;
	    		score=ccc;
	    }

	    public Key getKey1(){
	    return key1;
	    }
	    public Key getKey2(){
	    return key2;
	    }
	    public Double getScore(){
	    return score;
	    }


}
