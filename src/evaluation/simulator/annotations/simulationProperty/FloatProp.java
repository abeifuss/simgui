package evaluation.simulator.annotations.simulationProperty;

import javax.swing.JOptionPane;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import evaluation.simulator.conf.service.SimulationConfigService;

public class FloatProp extends SimProp {
	private static Logger logger = Logger.getLogger(FloatProp.class);

	private float maxValue;
	private float minValue;
	private float value;

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
}
