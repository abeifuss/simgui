package evaluation.simulator.gui.service;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class DescriptionService extends Observable {

	private static DescriptionService _instance = null;

	public static DescriptionService getInstance() {
		if (_instance == null) {
			_instance = new DescriptionService();
		}
		return _instance;
	}

	private String _description;

	private final List<Observer> _observers;

	private DescriptionService() {
		super();
		this._observers = new LinkedList<Observer>();
	}

	@Override
	public void addObserver(Observer o) {
		this._observers.add(o);
	}

	@Override
	public void notifyObservers() {
		Iterator<Observer> iter = this._observers.iterator();
		while (iter.hasNext()) {
			iter.next().update(this, this._description);
		}
	}

	public void setDescription(String descr) {
		this._description = descr;
		this.notifyObservers();
	}

}
