package Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.*;
import java.util.Map;

import Blocks.Block;
import Blocks.BlockRecognizer;

public class BlockSet {

	private ValueSet valueSet;
	private ArrayList<Block> blockList;
	private ArrayList<String> UnitMeasures = new ArrayList<String>();
	private String UnitMeasure = new String();

	/**
	 * set is constructed using a set of products
	 * 
	 * @param list,
	 *            the list of products
	 */
	public BlockSet(ValueSet vSet) {

		valueSet = vSet;

		blockList = new ArrayList<Block>();

		BlockRecognizer r = new BlockRecognizer();

		// for loop over all properties of all products to determine the
		// relevant properties to be added
		for (Value v : vSet.getValueSet()) {
			for (int i = 0; i < v.getNrBlocks(); i++) {
				blockList.add(r.createBlock(v.getBlock(i)));
				if (r.createBlock(v.getBlock(i)).getType().equals("Unit Measure")) {
					UnitMeasures.add(r.createBlock(v.getBlock(i)).getBlock());
				}
			}
		}
	}

	public ValueSet getValueSet() {
		return valueSet;
	}

	public ArrayList<Block> getBlockSet() {
		return blockList;
	}

	public String getKey() {
		return valueSet.getKey();
	}

	public int getNrStrings() {
		int i = 0;
		for (Block b : blockList) {
			if (b.getType().equals("String")) {
				i++;
			}
		}
		return i;

	}

	public int getNrUnitMeasures() {
		int i = 0;
		for (Block b : blockList) {
			if (b.getType().equals("Unit Measure")) {
				i++;
			}
		}
		return i;

	}

	public int getNrDoubles() {
		int i = 0;
		for (Block b : blockList) {
			if (b.getType().equals("Numerical")) {
				i++;
			}
		}
		return i;

	}

	public String getUnitMeasure() {
		Map<String, Integer> map = new HashMap<>();

		for (String t : UnitMeasures) {
			Integer val = map.get(t);
			map.put(t, val == null ? 1 : val + 1);
		}

		Entry<String, Integer> max = null;

		for (Entry<String, Integer> e : map.entrySet()) {
			if (max == null || e.getValue() > max.getValue())
				max = e;
		}

		return max.getKey();
	}

}
