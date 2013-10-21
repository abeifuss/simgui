package evaluation.simulator.gui.service;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class DescriptionService extends Observable{

	private String _description;
	private List<Observer> _observers;
	
	private static DescriptionService _instance = null;

	private DescriptionService() {
		super();
		_observers = new LinkedList<Observer>();
	}

	public static DescriptionService getInstance() {
		if (_instance == null) {
			_instance = new DescriptionService();
		}
		return _instance;
	}
	
	public void addObserver(Observer o){
		_observers.add(o);
	}
	
	public void notifyObservers(){
		Iterator<Observer> iter = _observers.iterator();
		while (iter.hasNext()) {
			iter.next().update(this, _description);
		}
	}
	
	public void setDescription(String descr){
		_description = descr;
		notifyObservers();
	}

}
