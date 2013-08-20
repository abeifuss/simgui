package annotations;

import javax.swing.JOptionPane;

public class FloatProp extends SimProp{

	float value;
	float minValue;
	float maxValue;

	@Override
	public Object getValue() {
		return value;
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
			value = tmp;
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
		return minValue;
	}

	public void setMinValue(float minValue) {
		this.minValue = minValue;
	}

	public float getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(float maxValue) {
		this.maxValue = maxValue;
	}
}
