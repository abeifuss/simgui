package gui.service;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class DescriptionService extends Observable{

	String description;
	List<Observer> observers;
	
	private static DescriptionService instance = null;

	private DescriptionService() {
		super();
		observers = new LinkedList<Observer>();
	}

	public static DescriptionService getInstance() {
		if (instance == null) {
			instance = new DescriptionService();
		}
		return instance;
	}
	
	public void addObserver(Observer o){
		observers.add(o);
	}
	
	public void notifyObservers(){
		Iterator<Observer> iter = observers.iterator();
		while (iter.hasNext()) {
			iter.next().update(this, description);
		}
	}
	
	public void setDescription(String descr){
		description = descr;
		notifyObservers();
	}

}
