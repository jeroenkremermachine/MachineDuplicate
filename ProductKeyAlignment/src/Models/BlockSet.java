package Models;

import java.util.ArrayList;

import Blocks.Block;
import Blocks.BlockRecognizer;

public class BlockSet {



	private ValueSet valueSet;
	private ArrayList<Block> blockList;

	/**
	 * set is constructed using a set of products
	 * @param list, the list of products
	 */
	public BlockSet(ValueSet vSet){

		valueSet = vSet;

		blockList = new ArrayList<Block>(); 

		BlockRecognizer r = new BlockRecognizer();

		//for loop over all properties of all products to determine the relevant properties to be added
		for( Value v : vSet.getValueSet()){
			for (int i = 0; i < v.getNrBlocks(); i++)
				blockList.add(r.createBlock(v.getBlock(i)));
			}
		}

	

	public ValueSet getValueSet(){
		return valueSet;
	}

	public ArrayList<Block> getBlockSet(){
		return blockList;
	}

	public String getKey(){
		return valueSet.getKey();
	}
	
	public int getNrStrings(){
		int i = 0;
		for (Block b : blockList){
			if(b.getType().equals("String")){
				i++;
			}
		}
		return i;
			
	}
	
	public int getNrUnitMeasures(){
		int i = 0;
		for (Block b : blockList){
			if(b.getType().equals("Unit Measure")){
				i++;
			}
		}
		return i;
			
	}
	
	public int getNrDoubles(){
		int i = 0;
		for (Block b : blockList){
			if(b.getType().equals("Double")){
				i++;
			}
		}
		return i;
			
	}
}
