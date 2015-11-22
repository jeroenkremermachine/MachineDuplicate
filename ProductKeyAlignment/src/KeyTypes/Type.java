package KeyTypes;

import Blocks.Block;
import Models.ValueSet;

/**
* interface class for types, it describes the relevant aspects of the specific types
* @author Thomas
*
*/
public interface Type {
		
		/**
		 * method that returns name of type
		 * @return
		 */
		public String getType();
		
		/**
		 * method that gives the three weights used for the similarity score
		 * @return
		 */
		
		public String getSubType();

		public String getKey();
}
