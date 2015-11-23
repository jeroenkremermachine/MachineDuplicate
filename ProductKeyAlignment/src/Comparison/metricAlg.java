package Comparison;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.QGramsDistance;
import uk.ac.shef.wit.simmetrics.tokenisers.InterfaceTokeniser;
import uk.ac.shef.wit.simmetrics.tokenisers.TokeniserQGram2;

public class metricAlg {
	private static InterfaceTokeniser tokeniser = new TokeniserQGram2();
	private static AbstractStringMetric metric = new QGramsDistance(tokeniser);

	public static double similarity(String k1, String k2) {
		boolean key_contains1 = false;
		boolean key_contains2 = false;
		double max_contained_score = 0.75;
		double value = metric.getSimilarity((k1), (k2));
		key_contains1 = k1.contains(k2);
		key_contains2 = k2.contains(k1);

		if (key_contains1 | key_contains2) {
			value = java.lang.Math.max(value, max_contained_score);
		}

		return value;
	}

	public static double covering(double p1, double p2, double productnr1, double productnr2) {

		double cover1 = (p1 / productnr1);
		double cover2 = (p2 / productnr2);
		double cov_score = -java.lang.Math.pow(cover1 - cover2, 2.0);
		return cov_score;
	}

	public static double diversit(double d1, double d2) {
		double div_score = 0;
		
		return div_score = -java.lang.Math.pow(d1-d2, 2.0);
		//if (java.lang.Math.min(d1, d2) == 1) {
		//	straf = -1;
		//} else {
		//	straf = 0;
		//}
		//return straf;
	}

	public static double getJacardSimilarityDouble(ArrayList<Double> a, ArrayList<Double> b) {
		// System.out.println(a.size() + "   " + b.size());
		Set<Double> x = new HashSet<Double>(a);
		Set<Double> y = new HashSet<Double>(b);

		int xSize = x.size();
		int ySize = y.size();
		// System.out.println(xSize + "  " + ySize);
		if (xSize == 0 || ySize == 0) {
			return 0.0;
		}

		Set<Double> intersectionXY = new HashSet<Double>(x);
		intersectionXY.retainAll(y);
		// System.out.println(intersectionXY.size());
		return (double) intersectionXY.size() / (double) (xSize < ySize ? xSize : ySize);
		//return (double) intersectionXY.size()/(xSize+ySize-intersectionXY.size());

	}

	public static double jaccard_similarity(ArrayList<String> x_raw, ArrayList<String> y_raw) {
		// System.out.println(x_raw.size() + "   " + y_raw.size());
		Set<String> x = new HashSet<String>(x_raw);
		Set<String> y = new HashSet<String>(y_raw);

		int xSize = x.size();
		int ySize = y.size();
		// System.out.println(xSize + "  " + ySize);
		if (xSize == 0 || ySize == 0) {
			return 0.0;
		}

		Set<String> intersectionXY = new HashSet<String>(x);
		intersectionXY.retainAll(y);
		// System.out.println(intersectionXY.size());
		return (double) intersectionXY.size() / (double) (xSize < ySize ? xSize : ySize);

	}
}
