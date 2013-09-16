package annotations;

import javax.swing.JOptionPane;

public class IntProp extends SimProp{

	private int _value;
	private int _minValue;
	private int _maxValue;

	@Override
	public Object getValue() {
		return _value;
	}

	@Override
	public void setValue(Object o) {
		int tmp = (int)(o);
		if (tmp <= getMaxValue() && tmp >= getMinValue() ){
			_value = tmp;
			return;
		}
		
		JOptionPane.showMessageDialog(null,
			    "This value is not in range.",
			    "Boundary error",
			    JOptionPane.ERROR_MESSAGE);
	}
	
	@Override
	public Class<?> getValueType() {
		return Integer.class;
	}
	
	public int getMinValue() {
		return _minValue;
	}

	public void setMinValue(int minValue) {
		this._minValue = minValue;
	}

	public int getMaxValue() {
		return _maxValue;
	}

	public void setMaxValue(int maxValue) {
		this._maxValue = maxValue;
	}

}
