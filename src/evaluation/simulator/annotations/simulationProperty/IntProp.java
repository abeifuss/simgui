package evaluation.simulator.annotations.simulationProperty;

import javax.swing.JOptionPane;

public class IntProp extends SimProp {

	private int _maxValue;
	private int _minValue;
	private int _value;

	public int getMaxValue() {
		return this._maxValue;
	}

	public int getMinValue() {
		return this._minValue;
	}

	@Override
	public Object getValue() {
		return this._value;
	}

	@Override
	public Class<?> getValueType() {
		return Integer.class;
	}

	public void setMaxValue(int maxValue) {
		this._maxValue = maxValue;
	}

	public void setMinValue(int minValue) {
		this._minValue = minValue;
	}

	@Override
	public void setValue(Object o) {
		int tmp = (int) (o);
		if ((tmp <= this.getMaxValue()) && (tmp >= this.getMinValue())) {
			this._value = tmp;
			return;
		}

		JOptionPane.showMessageDialog(null, "This value is not in range.",
				"Boundary error", JOptionPane.ERROR_MESSAGE);
	}

}
