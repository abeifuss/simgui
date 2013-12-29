package evaluation.simulator.annotations.property;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class BoolProp extends SimProp {

	private boolean value;
	
	private List<Observer> Observers = new LinkedList<Observer>();

	@Override
	public Object getValue() {
		return this.value;
	}

	@Override
	public Class<?> getValueType() {
		return Boolean.class;
	}

	@Override
	public void setValue(Object o) {
		this.value = (boolean) (o);
	}

	@Override
	public String toString() {
		return super.getName() + "" + this.value;
	}

	@Override
	public void register(Observer observer){
		System.err.println("REGISTER BOOL OBSERVER");
		Observers.add(observer);
	}
	
	@Override
	public void unregister(Observer observer){
		System.err.println("UNREGISTER BOOL OBSERVER");
		Observers.remove(observer);
	}

	@Override
	public void changed() {
		System.err.println("CHANGED BOOL");
		for ( Observer observer : Observers ) {
			observer.update((Observable) this, (Object) this.enabled);
		} 
	}

}
