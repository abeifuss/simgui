package evaluation.simulator.annotations.simulationProperty;

import javax.swing.JOptionPane;

import evaluation.simulator.log.LogLevel;
import evaluation.simulator.log.Logger;

public class FloatProp extends SimProp {

	private float _maxValue;
	private float _minValue;
	private float _value;

	public float getMaxValue() {
		return this._maxValue;
	}

	public float getMinValue() {
		return this._minValue;
	}

	@Override
	public Object getValue() {
		return this._value;
	}

	@Override
	public Class<?> getValueType() {
		return Float.class;
	}

	public void setMaxValue(float maxValue) {
		this._maxValue = maxValue;
	}

	public void setMinValue(float minValue) {
		this._minValue = minValue;
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
			this._value = tmp;
			return;
		}

		Logger.Log(LogLevel.ERROR, "For " + super.getId() + " Value not in rage! " + tmp + "(float) is not in (" + this.getMinValue() +", "+ this.getMaxValue() + ")");
		JOptionPane.showMessageDialog(null, "This value is not in range.",
				"Boundary error", JOptionPane.ERROR_MESSAGE);
	}
}
