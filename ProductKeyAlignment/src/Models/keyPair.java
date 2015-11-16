package Models;

public class keyPair {
	private Key key1;
	private Key key2;
	private Double score;

	public keyPair(Key aaa, Key bbb, Double ccc) {
		key1 = aaa;
		key2 = bbb;
		score = ccc;
	}

	public Key getKey1() {
		return key1;
	}

	public Key getKey2() {
		return key2;
	}

	public Double getScore() {
		return score;
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
