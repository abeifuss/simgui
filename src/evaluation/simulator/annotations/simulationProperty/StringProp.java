package evaluation.simulator.annotations.simulationProperty;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class StringProp extends SimProp {

	String possibleValues;
	String value;
	
	private List<Observer> Observers = new LinkedList<Observer>();
	
	public String getPossibleValues() {
		return this.possibleValues;
	}

	@Override
	public Object getValue() {
		return this.value;
	}

	@Override
	public Class<?> getValueType() {
		return String.class;
	}

	public void setPossibleValues(String values) {
		this.possibleValues = values;
	}

	@Override
	public void setValue(Object o) {
		this.value = (String) (o);
		changed();
	}
	
	@Override
	public String toString() {
		return super.getName() + "" + this.value;
	}

	@Override
	public void register(Observer observer){
		System.err.println("REGISTER STRING OBSERVER");
		Observers.add(observer);
	}
	
	@Override
	public void unregister(Observer observer){
		System.err.println("UNREGISTER STRING OBSERVER");
		Observers.remove(observer);
	}

	@Override
	public void changed() {
		System.err.println("CHANGED STRING");
		for ( Observer observer : Observers ) {
			observer.update((Observable) this, (Object) this.enabled);
		} 
	}
}
