package evaluation.simulator.gui.helper;

import java.util.Comparator;

import evaluation.simulator.annotations.property.SimProp;

/**
 * Comparing two {@link SimProp}
 * 
 * @author nachkonvention
 * 
 */
public class SimpropComparator implements Comparator<SimProp> {

	@Override
	public int compare(SimProp o1, SimProp o2) {
		return o1.getName().compareTo(o2.getName());
	}

}
