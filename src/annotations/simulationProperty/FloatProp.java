package annotations.simulationProperty;

import javax.swing.JOptionPane;

public class FloatProp extends SimProp{

	private float _value;
	private float _minValue;
	private float _maxValue;

	@Override
	public Object getValue() {
		return _value;
	}

	@Override
	public void setValue(Object o) {
		float tmp;
		
		if ( o instanceof Integer ){
			tmp = new Float((int)(o));
		}else if ( o instanceof String ) {
			tmp = Float.parseFloat((String)(o));
		}else{
			tmp = (float)(o);
		}
		
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
		return Float.class;
	}
	
	public float getMinValue() {
		return _minValue;
	}

	public void setMinValue(float minValue) {
		this._minValue = minValue;
	}

	public float getMaxValue() {
		return _maxValue;
	}

	public void setMaxValue(float maxValue) {
		this._maxValue = maxValue;
	}
}
