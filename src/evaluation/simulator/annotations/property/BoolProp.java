package evaluation.simulator.annotations.property;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * This class represents boolean simprops
 * 
 * @author alex
 *
 */
public class BoolProp extends SimProp {

	private boolean value;
	
	private List<Observer> Observers = new LinkedList<Observer>();

	/* (non-Javadoc)
	 * @see evaluation.simulator.annotations.property.SimProp#getValue()
	 */
	@Override
	public Object getValue() {
		return this.value;
	}

	/* (non-Javadoc)
	 * @see evaluation.simulator.annotations.property.SimProp#getValueType()
	 */
	@Override
	public Class<?> getValueType() {
		return Boolean.class;
	}

	/* (non-Javadoc)
	 * @see evaluation.simulator.annotations.property.SimProp#setValue(java.lang.Object)
	 */
	@Override
	public void setValue(Object o) {
		this.value = (boolean) (o);
	}

	/* (non-Javadoc)
	 * @see evaluation.simulator.annotations.property.SimProp#toString()
	 */
	@Override
	public String toString() {
		return super.getName() + "" + this.value;
	}

	/* (non-Javadoc)
	 * @see evaluation.simulator.annotations.property.SimProp#register(java.util.Observer)
	 */
	@Override
	public void register(Observer observer){
//		System.err.println("REGISTER BOOL OBSERVER");
		Observers.add(observer);
	}
	
	@Override
	public void unregister(Observer observer){
//		System.err.println("UNREGISTER BOOL OBSERVER");
		Observers.remove(observer);
	}

	/* (non-Javadoc)
	 * @see evaluation.simulator.annotations.property.SimProp#changed()
	 */
	@Override
	public void changed() {
//		System.err.println("CHANGED BOOL");
		for ( Observer observer : Observers ) {
			observer.update((Observable) this, (Object) this.enabled);
		} 
	}

}
