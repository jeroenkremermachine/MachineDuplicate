package Comparison;

import org.apache.commons.math3.stat.inference.TTest;
import java.util.*;

/**
 * Write a description of class Ttest here.
 * 
 * @author (your name)
 * @version (a version number or a date)
 */
public class Ttest {
	TTest studentTest = new TTest();

	public Ttest() {

	}

	public double getp(ArrayList<Double> a, ArrayList<Double> b) {

		double[] ValuesA = new double[a.size()];
		double[] ValuesB = new double[b.size()];
		for (int i = 0; i < ValuesA.length; i++) {
			ValuesA[i] = a.get(i);
		}
		for (int i = 0; i < ValuesB.length; i++) {
			ValuesB[i] = b.get(i);
		}
		double p_value = studentTest.tTest(ValuesA, ValuesB);
		return p_value;
	}

}