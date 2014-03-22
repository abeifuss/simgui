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

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(SimProp o1, SimProp o2) {
		
		if (o1.getPosition() != o2.getPosition()) {
			
			if (o1.getPosition() > o2.getPosition() )
				return -1;
			
			return 1;
			
		}
		return o1.getName().compareTo(o2.getName());
	}

}
