package evaluation.simulator.annotations.simulationProperty;

import javax.swing.JOptionPane;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import evaluation.simulator.conf.service.SimulationConfigService;

public class IntProp extends SimProp {
	
	private static Logger logger = Logger.getLogger(IntProp.class);

	private int maxValue;
	private int minValue;
	private int value;

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
		int tmp = (int) (o);
		if ((tmp <= this.getMaxValue()) && (tmp >= this.getMinValue())) {
			this.value = tmp;
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

}
