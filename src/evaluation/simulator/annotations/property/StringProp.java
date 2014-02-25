package evaluation.simulator.annotations.property;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * This class represents an string simprop
 * 
 * @author alex
 *
 */
public class StringProp extends SimProp {

	String possibleValues;
	String value;
	boolean multiSelection;
	
	private List<Observer> Observers = new LinkedList<Observer>();
	
	/**
	 * @return
	 * 		a string with possible values (comma seperated)
	 */
	public String getPossibleValues() {
		return this.possibleValues;
	}

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
		return String.class;
	}

	/**
	 * Sets the possible values
	 * 
	 * @param values
	 */
	public void setPossibleValues(String values) {
		this.possibleValues = values;
	}

	/* (non-Javadoc)
	 * @see evaluation.simulator.annotations.property.SimProp#setValue(java.lang.Object)
	 */
	@Override
	public void setValue(Object o) {
		this.value = (String) (o);
		changed();
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
//		System.err.println("REGISTER STRING OBSERVER");
		Observers.add(observer);
	}
	
	/* (non-Javadoc)
	 * @see evaluation.simulator.annotations.property.SimProp#unregister(java.util.Observer)
	 */
	@Override
	public void unregister(Observer observer){
//		System.err.println("UNREGISTER STRING OBSERVER");
		Observers.remove(observer);
	}

	/* (non-Javadoc)
	 * @see evaluation.simulator.annotations.property.SimProp#changed()
	 */
	@Override
	public void changed() {
//		System.err.println("CHANGED STRING");
		for ( Observer observer : Observers ) {
			observer.update((Observable) this, (Object) this.enabled);
		} 
	}

	/**
	 * Enables or disables multiselection
	 * 
	 * @param multiSelection
	 */
	public void setMultiSelection(boolean multiSelection) {
		this.multiSelection = multiSelection;
		
	}
	
	/**
	 * @return
	 * 		true if multiselection is allowed, otherwise false
	 */
	public boolean getMultiSelection() {
		return multiSelection;
		
	}
}
