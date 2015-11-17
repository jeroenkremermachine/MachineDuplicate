package Blocks;

/**
 * interface to model a block
 * @author Thomas
 *
 */

public interface Block {
	
	
	/**
	 * method that returns the string corresponding to the block
	 * @return
	 */
	public String getBlock();
	
	/**
	 * methods that returns the type of the block (see NameListBlocks class)
	 * @return
	 */
	public String getType();

}
