package evaluation.simulator.annotations.simulationProperty;

import javax.swing.JOptionPane;

public class DoubleProp extends SimProp {

	private double _maxValue;
	private double _minValue;
	private double _value;

	public double getMaxValue() {
		return this._maxValue;
	}

	public double getMinValue() {
		return this._minValue;
	}

	@Override
	public Object getValue() {
		return this._value;
	}

	@Override
	public Class<?> getValueType() {
		return Double.class;
	}

	public void setMaxValue(double maxValue) {
		this._maxValue = maxValue;
	}

	public void setMinValue(double minValue) {
		this._minValue = minValue;
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
			this._value = tmp;
			return;
		}

		JOptionPane.showMessageDialog(null, "This value is not in range.",
				"Boundary error", JOptionPane.ERROR_MESSAGE);
	}
}