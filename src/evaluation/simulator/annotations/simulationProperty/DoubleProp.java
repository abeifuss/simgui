package evaluation.simulator.annotations.simulationProperty;

import javax.swing.JOptionPane;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import evaluation.simulator.conf.service.SimulationConfigService;

public class DoubleProp extends SimProp {
	
	private static Logger logger = Logger.getLogger(DoubleProp.class);

	private double maxValue;
	private double minValue;
	private double value;
	
	private boolean auto;
	private boolean enableAuto;
	private boolean unlimited;
	private boolean enableUnlimited;
	
	private String guiElement;
	
	private double stepSize; 
	
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

	public double getMaxValue() {
		return this.maxValue;
	}

	public double getMinValue() {
		return this.minValue;
	}

	@Override
	public Object getValue() {
		return this.value;
	}

	@Override
	public Class<?> getValueType() {
		return Double.class;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}

	@Override
	public void setValue(Object o) {
		double tmp;

		if (o instanceof Integer) {
			tmp = new Double((int) (o));
		} else if (o instanceof String) {
			tmp = Double.parseDouble((String) (o));
		} else {
			tmp = (double) (o);
		}

		if ((tmp <= this.getMaxValue()) && (tmp >= this.getMinValue())) {
			this.value = tmp;
			return;
		}

		logger.log(Level.ERROR, "For " + super.getPropertyID() + " Value not in range! " + tmp + "(double) is not in (" + this.getMinValue() +", "+ this.getMaxValue() + ")");
		JOptionPane.showMessageDialog(null, "This value is not in range.",
				"Boundary error", JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public String toString() {
		return super.getName() + "" + this.value;
	}

	public double getStepSize() {
		return stepSize;
	}

	public void setStepSize(double stepSize) {
		this.stepSize = stepSize;
	}

	public String getGuiElement() {
		return guiElement;
	}

	public void setGuiElement(String guiElement) {
		this.guiElement = guiElement;
	}

}