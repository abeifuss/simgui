package evaluation.simulator.gui.helper;

import java.util.Comparator;
import java.util.Map;

/**
 * Comparing two Strings
 * 
 * @author nachkonvention
 * 
 */
public class ValueComparator implements Comparator<String> {

	Map<String, Integer> base;

	/**
	 * @param base
	 */
	public ValueComparator(Map<String, Integer> base) {
		this.base = base;
	}

	// Note: this comparator imposes orderings that are inconsistent with
	// equals.
	@Override
	public int compare(String a, String b) {
		if (this.base.get(a) <= this.base.get(b)) {
			return 1;
		} else {
			return -1;
		}
	}
}