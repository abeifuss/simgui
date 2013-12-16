package evaluation.simulator.annotations.simulationProperty;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import evaluation.simulator.conf.service.SimulationConfigService;

public class FloatProp extends SimProp {
	private static Logger logger = Logger.getLogger(FloatProp.class);

	private float maxValue;
	private float minValue;
	private float value;
	
	private boolean auto;
	private boolean enableAuto;
	private boolean unlimited;
	private boolean enableUnlimited;
	
	private String guiElement;
	
	private float stepSize; 
	
	private List<Observer> Observers = new LinkedList<Observer>();
	
	
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

	public float getMaxValue() {
		return this.maxValue;
	}

	public float getMinValue() {
		return this.minValue;
	}

	@Override
	public Object getValue() {
		return this.value;
	}

	@Override
	public Class<?> getValueType() {
		return Float.class;
	}

	public void setMaxValue(float maxValue) {
		this.maxValue = maxValue;
	}

	public void setMinValue(float minValue) {
		this.minValue = minValue;
	}

	@Override
	public void setValue(Object o) {
		float tmp;

		if (o instanceof Integer) {
			tmp = new Float((int) (o));
		} else if (o instanceof String) {
			tmp = Float.parseFloat((String) (o));
		} else {
			tmp = (float) (o);
		}

		if ((tmp <= this.getMaxValue()) && (tmp >= this.getMinValue())) {
			this.value = tmp;
			return;
		}

		logger.log(Level.ERROR, "For " + super.getPropertyID() + " Value not in rage! " + tmp + "(float) is not in (" + this.getMinValue() +", "+ this.getMaxValue() + ")");
		JOptionPane.showMessageDialog(null, "This value is not in range.",
				"Boundary error", JOptionPane.ERROR_MESSAGE);
	}
	
	@Override
	public String toString() {
		return super.getName() + "" + this.value;
	}

	public float getStepSize() {
		return stepSize;
	}

	public void setStepSize(float stepSize) {
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
		System.err.println("REGISTER FLOAT OBSERVER");
		Observers.add(observer);
	}
	
	@Override
	public void unregister(Observer observer){
		System.err.println("UNREGISTER FLOAT OBSERVER");
		Observers.remove(observer);
	}

	@Override
	public void changed() {
		System.err.println("CHANGED FLOAT");
		for ( Observer observer : Observers ) {
			observer.update((Observable) this, (Object) this.enabled);
		} 
	}

}
