package annotations;

import javax.swing.JOptionPane;

public class IntProp extends SimProp{

	int value;
	int minValue;
	int maxValue;

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public void setValue(Object o) {
		int tmp = (int)(o);
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
		return Integer.class;
	}
	
	public int getMinValue() {
		return minValue;
	}

	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}

	public int getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

}
