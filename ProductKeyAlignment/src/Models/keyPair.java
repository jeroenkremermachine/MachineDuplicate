package Models;

public class keyPair {
	private Key key1;
	private Key key2;
	private double nameScore = 0;
	private double covScore= 0;
	private double divScore = 0;
	private double stringScore= 0;
	private double isString = 0;
	private double doubleScore = 0;
	private double subTypeScore =0;
	private double unitScore = 0;
	private boolean isGolden = false;
	

	public keyPair(Key aaa, Key bbb) {
		key1 = aaa;
		key2 = bbb;
	}

	public Key getKey1() {
		return key1;
	}

	public Key getKey2() {
		return key2;
	}


	public void setNameScore(double s){
		nameScore = s;
	}
	
	public double getNameScore(){
		return nameScore;
	}
	
	public void setCovScore(double s){
		covScore = s;
	}
	
	public double getCovScore(){
		return covScore;
	}
	
	public void setDivScore(double s){
		divScore = s;
	}
	
	public double getDivScore(){
		return divScore;
	}
	
	public void setStringScore(double s){
		stringScore = s;
	}
	
	public double getStringScore(){
		return stringScore;
	}
	
	public void setIsString(double s){
		isString = s;
	}
	
	public double getIsString(){
		return isString;
	}
	
	public void setDoubleScore(double s){
		doubleScore = s;
	}
	
	public double getDoubleScore(){
		return doubleScore;
	}
	
	public void setSubTypeScore(double s){
		subTypeScore = s;
	}
	
	public double getSubTypeScore(){
		return subTypeScore;
	}
	
	public void setUnitScore(double s){
		unitScore = s;
	}
	
	public double getUnitScore(){
		return unitScore;
	}
	
	public void setIsGolden(boolean s){
		isGolden = s;
	}
	
	public boolean getIsGolden(){
		return isGolden;
	}
	
	
	

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}

		keyPair guest = (keyPair) obj;
		if (key1.getName().equals(guest.getKey1().getName()) && key2.getName().equals(guest.getKey2().getName())) {
			return true;
		} else if (key1.getName().equals(guest.getKey2().getName())
				&& key2.getName().equals(guest.getKey1().getName())) {
			return true;
		} else {
			return false;
		}
	}

}
