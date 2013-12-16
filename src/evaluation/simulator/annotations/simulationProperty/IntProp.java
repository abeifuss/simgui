package evaluation.simulator.annotations.simulationProperty;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import evaluation.simulator.conf.service.SimulationConfigService;

public class IntProp extends SimProp {
	
	private static Logger logger = Logger.getLogger(IntProp.class);

	private int maxValue;
	private int minValue;
	private int value;

	private boolean auto;
	private boolean enableAuto;
	private boolean unlimited;
	private boolean enableUnlimited;
	
	private List<Observer> Observers = new LinkedList<Observer>();
	
	private String guiElement;
	
	private int stepSize; 
	
	public boolean getAuto(){
		return this.auto;
	}
	
	public void setAuto( boolean auto ){
		this.auto = auto;
	}
	
	public boolean getEnableAuto(){
		return this.enableAuto;
	}
	
	public void setEnableAuto( boolean auto ){
		this.enableAuto = auto;
	}
	
	public boolean getUnlimited(){
		return this.unlimited;
	}
	
	public void setUnlimited( boolean unlimited ){
		this.unlimited = unlimited;
	}
	
	public boolean getEnableUnlimited(){
		return this.enableUnlimited;
	}
	
	public void setEnableUnlimited( boolean unlimited ){
		this.enableUnlimited = unlimited;
	}

	public int getMaxValue() {
		return this.maxValue;
	}

	public int getMinValue() {
		return this.minValue;
	}

	@Override
	public Object getValue() {
		return this.value;
	}

	@Override
	public Class<?> getValueType() {
		return Integer.class;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}

	@Override
	public void setValue(Object o) {
		System.err.println("CHANGED VALUE IN INTPROP");
		
		int tmp = (int) (o);
		if ((tmp <= this.getMaxValue()) && (tmp >= this.getMinValue())) {
			this.value = tmp;
			this.changed();
			return;
		}

		logger.log(Level.ERROR, "For " + super.getPropertyID() + " Value not in rage! " + tmp + "(int) is not in (" + this.getMinValue() +", "+ this.getMaxValue() + ")");
		JOptionPane.showMessageDialog(null, "This value is not in range.",
				"Boundary error", JOptionPane.ERROR_MESSAGE);
	}
	
	@Override
	public String toString() {
		return super.getName() + "" + this.value;
	}

	public int getStepSize() {
		return stepSize;
	}

	public void setStepSize(int stepSize) {
		this.stepSize = stepSize;
	}

	public String getGuiElement() {
		return guiElement;
	}

	public void setGuiElement(String guiElement) {
		this.guiElement = guiElement;
	}
	
	@Override
	public void register(Observer observer){
		System.err.println("REGISTER INTEGER OBSERVER");
		Observers.add(observer);
	}
	
	@Override
	public void unregister(Observer observer){
		System.err.println("UNREGISTER INTEGER OBSERVER");
		Observers.remove(observer);
	}

	@Override
	public void changed() {
		System.err.println("CHANGED");
		for ( Observer observer : Observers ) {
			observer.update((Observable) this, (Object) this.enabled);
		} 
	}

}
